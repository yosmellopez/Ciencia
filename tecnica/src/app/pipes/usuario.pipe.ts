import {Pipe, PipeTransform} from '@angular/core';
import {Usuario} from "../modelo.datos";

@Pipe({
    name: 'usuario'
})
export class UsuarioPipe implements PipeTransform {

    transform(value: Usuario, args?: any): any {
        return value == null ? "" : value.nombre + " " + value.apellidos;
    }

}
