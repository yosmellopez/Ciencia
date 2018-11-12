import {Component, OnInit, ViewChild} from '@angular/core';
import {SelectionModel} from "@angular/cdk/collections";
import {catchError, map, startWith, switchMap} from "rxjs/internal/operators";
import {Confirm, Information} from "../../mensaje/window.mensaje";
import {MatDialog, MatPaginator, MatSort, MatTable, MatTableDataSource} from "@angular/material";
import {forkJoin, merge, Subject} from "rxjs/index";
import {AutorWindow} from "./autor-window/autor-window.component";
import {AutorService} from "../../services/autor.service";
import {Autor} from "../../modelo.datos";

@Component({
    selector: 'app-autor',
    templateUrl: './autor.component.html',
    styleUrls: ['./autor.component.css']
})
export class AutorComponent implements OnInit {

    dataSource: MatTableDataSource<Autor> = new MatTableDataSource<Autor>();
    total: number = 0;
    pageSize: number = 10;
    displayedColumns = ['seleccionado', 'index', 'nombre', 'apellidos', 'acciones'];
    selection = new SelectionModel<Autor>(true, []);
    nombre: string = '';
    resultsLength = 0;
    isLoadingResults = true;
    cont = 0;
    @ViewChild(MatPaginator) paginator: MatPaginator;
    @ViewChild(MatSort) sort: MatSort;
    @ViewChild(MatTable) table: MatTable<Autor>;

    constructor(private service: AutorService, private dialog: MatDialog) {
    }

    ngOnInit() {
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
        this.sort.active = 'id,desc';
        this.paginator.pageSize = this.pageSize;
        this.inicializarElementos();
        this.sort.sortChange.subscribe(() => (this.paginator.pageIndex = 0));
        merge(this.sort.sortChange, this.paginator.page)
            .pipe(
                startWith({}),
                switchMap(() => {
                    this.isLoadingResults = true;
                    return this.service.listarAutores(this.sort.active, this.sort.direction, this.paginator.pageIndex, this.paginator.pageSize);
                }),
                map(data => {
                    this.total = data.body.total;
                    this.isLoadingResults = false;
                    this.cont = 0;
                    return data.body.elementos;
                }),
                catchError(data => {
                    return [];
                })
            )
            .subscribe(datos => {
                this.dataSource = new MatTableDataSource(datos);
                this.paginator.length = this.total;
                this.table.dataSource = this.dataSource;
                this.table.renderRows();
            });
    }

    abrirVentana() {
        let dialogRef = this.dialog.open(AutorWindow, {
            width: '400px', disableClose: true, data: new Autor(),
        });

        dialogRef.afterClosed().subscribe(result => {
            if (result != false) {
                this.dialog.open(Information, {width: '350px', data: {mensaje: 'Se ha insertardo el autor.'}});
                this.paginator.page.emit();
            }
        });
    }

    editarAutor(event: Event, autor: Autor): void {
        event.stopPropagation();
        let editDialogRef = this.dialog.open(AutorWindow, {
            width: '400px', data: autor, disableClose: true
        });

        editDialogRef.afterClosed().subscribe(result => {
            if (result != false && result.success) {
                this.dialog.open(Information, {
                    width: '350px',
                    data: {mensaje: 'Se ha modificado el autor.'}
                });
                this.paginator.page.emit();
            }
        });
    }

    eliminarAutor(event: Event, autor: Autor): void {
        event.stopPropagation();
        let dialogRef = this.dialog.open(Confirm, {
            width: '400px',
            data: {mensaje: 'Desea eliminar la autor:<br>- ' + autor.nombre},
        });
        dialogRef.afterClosed().subscribe(result => {
            if (result) {
                this.service.eliminarAutor(autor.id).subscribe(resp => {
                    if (resp.body.success) {
                        this.dialog.open(Information, {
                            width: '350px',
                            data: {mensaje: 'Se ha eliminado el autor.'}
                        });
                        this.selection.clear();
                        this.paginator.page.emit();
                    }
                });
            }
        });
    }

    eliminarAutors(event: Event): void {
        event.stopPropagation();
        let autores = this.selection.selected;
        if (autores.length === 0) {
            this.dialog.open(Information, {width: '320px', data: {mensaje: "No se han seleccionado elementos"}});
        } else {
            let dialogRef = this.dialog.open(Confirm, {
                width: '400px',
                data: {mensaje: 'Desea eliminar los autores:<br>- ' + autores.map(autor => autor.nombre).join("<br> -")},
            });
            dialogRef.afterClosed().subscribe(result => {
                if (result) {
                    let allProgressObservables = [];
                    const todos = new Subject<boolean>();
                    autores.forEach(autor => {
                        allProgressObservables.push(this.service.eliminarAutor(autor.id));
                    });
                    forkJoin(allProgressObservables).subscribe(response => {
                        let completo = true;
                        response.forEach(resp => {
                            if (!resp.body.success) {
                                completo = false;
                            }
                        });
                        todos.next(completo);
                    });
                    todos.subscribe(value => {
                        this.dialog.open(Information, {
                            width: '350px',
                            data: {mensaje: value ? 'Se ha eliminado todos los autores.' : 'No se eliminaron correctamente todos los autores'}
                        });
                        this.selection.clear();
                        this.paginator.page.emit();
                    });
                }
            });
        }
    }

    isAllSelected() {
        const numSelected = this.selection.selected.length;
        const numRows = this.dataSource.data.length;
        return numSelected === numRows;
    }

    masterToggle() {
        this.isAllSelected() ? this.selection.clear() : this.dataSource.data.forEach(row => this.selection.select(row));
    }

    expandCollapse(event: Event, elemento: Autor) {
        event.stopPropagation();
    }

    private inicializarElementos(): void {
        this.paginator._intl.itemsPerPageLabel = "Registros por página";
        this.paginator._intl.firstPageLabel = "Primera página";
        this.paginator._intl.lastPageLabel = "Última página";
        this.paginator._intl.nextPageLabel = "Página siguiente";
        this.paginator._intl.previousPageLabel = "Página anterior";
        this.paginator._intl.getRangeLabel = (page: number, pageSize: number, length: number) => {
            if (length == 0 || pageSize == 0) {
                return `0 de ${length}`;
            }
            length = Math.max(length, 0);
            const startIndex = page * pageSize;
            const endIndex = startIndex < length ? Math.min(startIndex + pageSize, length) : startIndex + pageSize;
            return `${startIndex + 1} - ${endIndex} de ${length}`;
        }
    }

    showIndex(index, element) {
    }
}
