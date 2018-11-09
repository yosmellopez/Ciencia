import {HttpHeaders, HttpResponseBase} from "@angular/common/http";

export class Usuario {
  id: number;
  nombre: string;
  apellidos: string;
  usuario: string;
  contrasena: string;
  eliminado: boolean;
  rol: Rol
}

export class Rol {
  id: number;
  nombre: string;
}

export class ObjectParam<T> {
  object: T;
}

export class AppResponse<T> {
  success: boolean;
  msg: string;
  elemento?: T;
  elementos?: T[];
  total: number;
}

export class ResponseApp {
  success: boolean;
  msg: string;
  total: number;
}

export declare class Respuesta<T> extends HttpResponseBase {
  body: AppResponse<T>;
  headers: HttpHeaders;
  status: number;
  statusText: string;
  url: string;
}

export declare interface RouteInfo {
  path: string;
  title: string;
  icon: string;
  class: string;
  authority: string[];
}
