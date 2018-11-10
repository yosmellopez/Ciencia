import {Component, Inject, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from "@angular/material";
import {MensajeError} from "../../../mensaje/window.mensaje";
import {AreaService} from "../../../services/area.service";
import {Area} from "../../../modelo.datos";

@Component({
    selector: 'app-area-window',
    templateUrl: './area-window.component.html',
    styleUrls: ['./area-window.component.css']
})
export class AreaWindow implements OnInit {
    isLoadingResults = false;
    idUser: number;
    insertar = true;
    form: FormGroup;

    constructor(public dialogRef: MatDialogRef<AreaWindow>, @Inject(MAT_DIALOG_DATA) {id, nombre}: Area,
                private service: AreaService, private dialog: MatDialog) {
        if (id)
            this.insertar = false;
        this.idUser = id;
        this.form = new FormGroup({
            nombre: new FormControl(nombre, [Validators.required]),
        });
    }

    insertarArea(): void {
        if (this.form.valid) {
            this.isLoadingResults = true;
            if (this.insertar) {
                this.service.insertarArea(this.form.value).subscribe(resp => {
                    let appResp = resp.body;
                    if (appResp.success) {
                        this.dialogRef.close(resp.body);
                    } else {
                        this.dialog.open(MensajeError, {width: "400px", data: {mensaje: appResp.msg}});
                    }
                    this.isLoadingResults = false;
                });
            } else {
                this.service.modificarArea(this.idUser, this.form.value).subscribe(resp => {
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
