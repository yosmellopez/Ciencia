import {NgModule} from "@angular/core";
import {RolPipe} from "./rol.pipe";
import {UsuarioPipe} from './usuario.pipe';
import {GrupoPipe} from './grupo.pipe';
import {AreaPipe} from './area.pipe';
import {AutorPipe} from './autor.pipe';

@NgModule({
    declarations: [RolPipe, UsuarioPipe, GrupoPipe, AreaPipe, AutorPipe],
    exports: [RolPipe, UsuarioPipe, GrupoPipe, AreaPipe, AutorPipe]
})
export class PipesModule {

}
