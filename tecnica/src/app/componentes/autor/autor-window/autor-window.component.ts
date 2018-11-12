import {Component, Inject, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from "@angular/material";
import {MensajeError} from "../../../mensaje/window.mensaje";
import {AutorService} from "../../../services/autor.service";
import {Autor} from "../../../modelo.datos";

@Component({
    selector: 'app-autor-window',
    templateUrl: './autor-window.component.html',
    styleUrls: ['./autor-window.component.css']
})
export class AutorWindow implements OnInit {
    isLoadingResults = false;
    idUser: number;
    insertar = true;
    form: FormGroup;

    constructor(public dialogRef: MatDialogRef<AutorWindow>, @Inject(MAT_DIALOG_DATA) {id, nombre, apellidos}: Autor,
                private service: AutorService, private dialog: MatDialog) {
        if (id)
            this.insertar = false;
        this.idUser = id;
        this.form = new FormGroup({
            nombre: new FormControl(nombre, [Validators.required]),
            apellidos: new FormControl(apellidos, [Validators.required]),
        });
    }

    insertarAutor(): void {
        if (this.form.valid) {
            this.isLoadingResults = true;
            if (this.insertar) {
                this.service.insertarAutor(this.form.value).subscribe(resp => {
                    let appResp = resp.body;
                    if (appResp.success) {
                        this.dialogRef.close(resp.body);
                    } else {
                        this.dialog.open(MensajeError, {width: "400px", data: {mensaje: appResp.msg}});
                    }
                    this.isLoadingResults = false;
                });
            } else {
                this.service.modificarAutor(this.idUser, this.form.value).subscribe(resp => {
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
    }

    onNoClick(): void {
        this.dialogRef.close(false);
    }
}
