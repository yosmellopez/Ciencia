import {Pipe, PipeTransform} from '@angular/core';
import {Grupo} from "../modelo.datos";

@Pipe({
    name: 'grupo'
})
export class GrupoPipe implements PipeTransform {

    transform(value: Grupo, args?: any): any {
        return value == null ? "" : value.nombre;
    }

}
