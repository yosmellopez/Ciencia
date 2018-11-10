import { Pipe, PipeTransform } from '@angular/core';
import {Area} from "../modelo.datos";

@Pipe({
  name: 'area'
})
export class AreaPipe implements PipeTransform {

    transform(value: Area, args?: any): any {
        return value == null ? "" : value.nombre;
    }


}
