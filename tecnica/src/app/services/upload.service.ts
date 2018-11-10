import {Injectable} from '@angular/core';
import {HttpClient, HttpEventType, HttpRequest, HttpResponse} from "@angular/common/http";
import {Observable, Subject} from "rxjs/index";
import {SERVER_API_URL} from "../app.constant";
import {AppResponse, Imagen} from "../modelo.datos";

@Injectable({
    providedIn: 'root'
})
export class UploadService {

    private url = SERVER_API_URL + "imagen/upload";

    constructor(private http: HttpClient) {
    }

    public uploadFiles(files: Set<File>): { [key: string]: Observable<number> } {
        // this will be the our resulting map
        const status = {};

        files.forEach(file => {
            // create a new multipart-form for every file
            const formData: FormData = new FormData();
            formData.append('file', file, file.name);

            // create a http-post request and pass the form
            // tell it to report the upload progress
            const req = new HttpRequest('POST', this.url, formData, {
                reportProgress: true
            });

            // create a new progress-subject for every file
            const progress = new Subject<number>();
            const element = new Subject<AppResponse<Imagen>>();

            // send the http-request and subscribe for progress-updates
            this.http.request(req).subscribe(event => {
                if (event.type === HttpEventType.UploadProgress) {
                    const percentDone = Math.round(100 * event.loaded / event.total);
                    progress.next(percentDone);
                } else if (event instanceof HttpResponse) {
                    progress.complete();
                    let upload: AppResponse<Imagen> = <AppResponse<Imagen>> event.body;
                    element.next(upload);
                    element.complete();
                }
            });

            // Save every progress-observable in a map of all observables
            status[file.name] = {
                progress: progress.asObservable(),
                element: element.asObservable()
            };
        });

        // return the map of progress.observables
        return status;
    }
}
