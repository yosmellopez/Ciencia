import {Pipe, PipeTransform} from '@angular/core';
import {Autor} from "../modelo.datos";

@Pipe({
    name: 'autor'
})
export class AutorPipe implements PipeTransform {

    transform(value: Autor, args?: any): any {
        return value == null ? "" : value.nombre + " " + value.apellidos;
    }

}
