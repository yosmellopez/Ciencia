import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'autor'
})
export class AutorPipe implements PipeTransform {

  transform(value: any, args?: any): any {
    return null;
  }

}
