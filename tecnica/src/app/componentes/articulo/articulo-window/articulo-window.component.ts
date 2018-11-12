import {Component, ElementRef, Inject, OnInit, ViewChild} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from "@angular/material";
import {MensajeError} from "../../../mensaje/window.mensaje";
import {ArticuloService} from "../../../services/articulo.service";
import {Area, Articulo, Autor, Grupo} from "../../../modelo.datos";
import {AreaService} from "../../../services/area.service";
import {GrupoService} from "../../../services/grupo.service";
import {AutorService} from "../../../services/autor.service";

@Component({
    selector: 'app-articulo-window',
    templateUrl: './articulo-window.component.html',
    styleUrls: ['./articulo-window.component.css']
})
export class ArticuloWindow implements OnInit {
    isLoadingResults = false;
    idUser: number;
    insertar = true;
    form: FormGroup;
    grupos: Grupo[] = [];
    areas: Area[] = [];
    autores: Autor[] = [];
    autoresFiltrados: Autor[] = [];
    @ViewChild("control")
    input: ElementRef;

    constructor(public dialogRef: MatDialogRef<ArticuloWindow>, @Inject(MAT_DIALOG_DATA) {id, titulo, area, grupo, year, autores}: Articulo,
                private service: ArticuloService, private dialog: MatDialog, private areaService: AreaService, private grupoService: GrupoService, private autorService: AutorService) {
        if (id)
            this.insertar = false;
        this.idUser = id;
        this.form = new FormGroup({
            titulo: new FormControl(titulo, [Validators.required]),
            year: new FormControl(year, [Validators.required]),
            area: new FormControl(area, [Validators.required]),
            grupo: new FormControl(grupo, [Validators.required]),
            autores: new FormControl(autores, [Validators.required]),
        });
    }

    insertarArticulo(): void {
        if (this.form.valid) {
            this.isLoadingResults = true;
            if (this.insertar) {
                this.service.insertarArticulo(this.form.value).subscribe(resp => {
                    let appResp = resp.body;
                    if (appResp.success) {
                        this.dialogRef.close(resp.body);
                    } else {
                        this.dialog.open(MensajeError, {width: "400px", data: {mensaje: appResp.msg}});
                    }
                    this.isLoadingResults = false;
                });
            } else {
                this.service.modificarArticulo(this.idUser, this.form.value).subscribe(resp => {
                    let appResp = resp.body;
                    if (appResp.success) {
                        this.dialogRef.close(resp.body);
                    } else {
                        this.dialog.open(MensajeError, {width: "400px", data: {mensaje: appResp.msg}});
                    }
                    this.isLoadingResults = false;
                });
            }
        }
    }

    ngOnInit() {
        this.grupoService.listarAllGrupos().subscribe(resp => {
            if (resp.body.success) {
                this.grupos = resp.body.elementos;
            }
        });
        this.areaService.listarAllAreas().subscribe(resp => {
            if (resp.body.success) {
                this.areas = resp.body.elementos;
            }
        });
        this.autorService.listarAllAutores().subscribe(resp => {
            if (resp.body.success) {
                this.autores = resp.body.elementos;
                this.autoresFiltrados = resp.body.elementos;
            }
        });
    }

    filtrarSelect(event: Event) {
        let value = this.input.nativeElement.value;
        this.autoresFiltrados = this.autores.filter(autor => autor.nombre.includes(value));
    }

    compararAreas(inicio: Area, fin: Area) {
        return inicio && fin && inicio.id === fin.id;
    }

    compararGrupos(inicio: Grupo, fin: Grupo) {
        return inicio && fin && inicio.id === fin.id;
    }

    onNoClick(): void {
        this.dialogRef.close(false);
    }
}
