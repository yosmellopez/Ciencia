import {Injectable} from '@angular/core';
import {Observable} from "rxjs/index";
import {SERVER_API_URL} from "../app.constant";
import {HttpClient} from "@angular/common/http";
import {AppResponse, Autor, ResponseApp, Respuesta} from "../modelo.datos";

@Injectable({providedIn: 'root'})
export class AutorService {

    private autorUrl = SERVER_API_URL + "api/autor";
    private token: string = "";

    constructor(private http: HttpClient) {
        this.token = localStorage.getItem("user_token");
    }

    listarAutores(sort: string, order: string, page: number, limit: number): Observable<Respuesta<Autor>> {
        let constUrl = `${this.autorUrl}?sort=${sort},${order}&page=${page + 1}&limit=${limit}`;
        return this.http.get<AppResponse<Autor>>(constUrl, {
            observe: "response",
            headers: {"Authorization": this.token}
        });
    }

    listarAllAutores(): Observable<Respuesta<Autor>> {
        let constUrl = `${this.autorUrl}/all`;
        return this.http.get<AppResponse<Autor>>(constUrl, {
            observe: "response",
            headers: {"Authorization": this.token}
        });
    }

    insertarAutor(autor: Autor): Observable<Respuesta<Autor>> {
        return this.http.post<AppResponse<Autor>>(this.autorUrl, autor, {
            observe: "response",
            headers: {"Authorization": this.token}
        });
    }

    modificarAutor(id: number, autor: Autor): Observable<Respuesta<Autor>> {
        autor.id = id;
        return this.http.put<AppResponse<Autor>>(this.autorUrl + "/" + id, autor, {
            observe: "response",
            headers: {"Authorization": this.token}
        });
    }

    eliminarAutor(id: number): Observable<Respuesta<ResponseApp>> {
        return this.http.delete<ResponseApp>(this.autorUrl + "/" + id, {
            observe: "response",
            headers: {"Authorization": this.token}
        });
    }
}
