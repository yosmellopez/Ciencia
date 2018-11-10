import {Component, Inject} from "@angular/core";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material";
import {UploadService} from "../services/upload.service";
import {forkJoin} from "rxjs/index";

@Component({
    selector: 'upload-window',
    templateUrl: './upload.window.html',
})
export class UploadWindow {
    archivos: Set<File>;
    progress;
    canBeClosed = true;
    primaryButtonText = 'Upload';
    showCancelButton = true;
    uploading = false;
    uploadSuccessful = false;

    constructor(public dialogRef: MatDialogRef<UploadWindow>, @Inject(MAT_DIALOG_DATA) public data: Set<File>, private uploadService: UploadService) {
        this.archivos = data;
    }

    cerrarDialog(): void {
        this.archivos.clear();
        this.dialogRef.close(false);
    }

    subirArchivos() {
        if (this.uploadSuccessful) {
            return this.dialogRef.close();
        }

        // set the component state to "uploading"
        this.uploading = true;

        // start the upload and save the progress map
        this.progress = this.uploadService.uploadFiles(this.archivos);

        // convert the progress map into an array
        let allProgressObservables = [];
        let allFilesObservables = [];
        for (let key in this.progress) {
            allProgressObservables.push(this.progress[key].progress);
            allFilesObservables.push(this.progress[key].element)
        }

        // Adjust the state variables

        // The OK-button should have the text "Finish" now
        this.primaryButtonText = 'Finish';

        // The dialog should not be closed while uploading
        this.canBeClosed = false;
        this.dialogRef.disableClose = true;

        // Hide the cancel-button
        this.showCancelButton = false;

        // When all progress-observables are completed...
        forkJoin(allProgressObservables).subscribe(end => {
            this.canBeClosed = true;
            this.dialogRef.disableClose = false;
            this.uploadSuccessful = true;
            this.uploading = false;
        });
        forkJoin(allFilesObservables).subscribe(response => {
            this.archivos.clear();
            this.dialogRef.close(response);
        });
    }
}
