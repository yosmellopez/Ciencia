import {Injectable} from '@angular/core';
import {Observable} from "rxjs/index";
import {SERVER_API_URL} from "../app.constant";
import {HttpClient} from "@angular/common/http";
import {AppResponse, Articulo, ResponseApp, Respuesta} from "../modelo.datos";

@Injectable({providedIn: 'root'})
export class ArticuloService {

    private articuloUrl = SERVER_API_URL + "api/articulo";
    private token: string = "";

    constructor(private http: HttpClient) {
        this.token = localStorage.getItem("user_token");
    }

    listarArticulos(sort: string, order: string, page: number, limit: number): Observable<Respuesta<Articulo>> {
        let constUrl = `${this.articuloUrl}?sort=${sort},${order}&page=${page + 1}&limit=${limit}`;
        return this.http.get<AppResponse<Articulo>>(constUrl, {
            observe: "response",
            headers: {"Authorization": this.token}
        });
    }

    listarAllArticulos(): Observable<Respuesta<Articulo>> {
        let constUrl = `${this.articuloUrl}/all`;
        return this.http.get<AppResponse<Articulo>>(constUrl, {
            observe: "response",
            headers: {"Authorization": this.token}
        });
    }

    insertarArticulo(articulo: Articulo): Observable<Respuesta<Articulo>> {
        return this.http.post<AppResponse<Articulo>>(this.articuloUrl, articulo, {
            observe: "response",
            headers: {"Authorization": this.token}
        });
    }

    modificarArticulo(id: number, articulo: Articulo): Observable<Respuesta<Articulo>> {
        articulo.id = id;
        return this.http.put<AppResponse<Articulo>>(this.articuloUrl + "/" + id, articulo, {
            observe: "response",
            headers: {"Authorization": this.token}
        });
    }

    eliminarArticulo(id: number): Observable<Respuesta<ResponseApp>> {
        return this.http.delete<ResponseApp>(this.articuloUrl + "/" + id, {
            observe: "response",
            headers: {"Authorization": this.token}
        });
    }
}
