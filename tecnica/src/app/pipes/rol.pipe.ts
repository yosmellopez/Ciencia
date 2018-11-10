import {Pipe, PipeTransform} from '@angular/core';
import {Rol} from "../modelo.datos";

@Pipe({
    name: 'rol'
})
export class RolPipe implements PipeTransform {

    transform(value: Rol, args?: any): any {
        return value == null ? "" : value.nombre;
    }

}
