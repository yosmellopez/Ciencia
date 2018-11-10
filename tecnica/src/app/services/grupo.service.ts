import {Injectable} from '@angular/core';
import {Observable} from "rxjs/index";
import {SERVER_API_URL} from "../app.constant";
import {HttpClient} from "@angular/common/http";
import {AppResponse, Grupo, Respuesta} from "../modelo.datos";

@Injectable({providedIn: 'root'})
export class GrupoService {

    private grupoUrl = SERVER_API_URL + "api/grupo";
    private token: string = "";

    constructor(private http: HttpClient) {
        this.token = localStorage.getItem("user_token");
    }

    listarAllGrupos(): Observable<Respuesta<Grupo>> {
        let constUrl = `${this.grupoUrl}`;
        return this.http.get<AppResponse<Grupo>>(constUrl, {
            observe: "response",
            headers: {"Authorization": this.token}
        });
    }


}
