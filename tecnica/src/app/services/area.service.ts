import {Injectable} from '@angular/core';
import {Observable} from "rxjs/index";
import {SERVER_API_URL} from "../app.constant";
import {HttpClient} from "@angular/common/http";
import {AppResponse, Area, ResponseApp, Respuesta} from "../modelo.datos";

@Injectable({providedIn: 'root'})
export class AreaService {

    private areaUrl = SERVER_API_URL + "api/area";
    private token: string = "";

    constructor(private http: HttpClient) {
        this.token = localStorage.getItem("user_token");
    }

    listarAreas(sort: string, order: string, page: number, limit: number): Observable<Respuesta<Area>> {
        let constUrl = `${this.areaUrl}?sort=${sort},${order}&page=${page + 1}&limit=${limit}`;
        return this.http.get<AppResponse<Area>>(constUrl, {
            observe: "response",
            headers: {"Authorization": this.token}
        });
    }

    listarAllAreas(): Observable<Respuesta<Area>> {
        let constUrl = `${this.areaUrl}/all`;
        return this.http.get<AppResponse<Area>>(constUrl, {
            observe: "response",
            headers: {"Authorization": this.token}
        });
    }

    insertarArea(area: Area): Observable<Respuesta<Area>> {
        return this.http.post<AppResponse<Area>>(this.areaUrl, area, {
            observe: "response",
            headers: {"Authorization": this.token}
        });
    }

    modificarArea(id: number, area: Area): Observable<Respuesta<Area>> {
        area.id = id;
        return this.http.put<AppResponse<Area>>(this.areaUrl + "/" + id, area, {
            observe: "response",
            headers: {"Authorization": this.token}
        });
    }

    eliminarArea(id: number): Observable<Respuesta<ResponseApp>> {
        return this.http.delete<ResponseApp>(this.areaUrl + "/" + id, {
            observe: "response",
            headers: {"Authorization": this.token}
        });
    }
}
