import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";
import {BrowserModule} from "@angular/platform-browser";
import {Route, RouterModule} from "@angular/router";
import {ArticuloComponent} from "./componentes/articulo/articulo.component";
import {AreaComponent} from "./componentes/area/area.component";
import {AutorComponent} from "./componentes/autor/autor.component";
import {UsuarioComponent} from "./componentes/usuario/usuario.component";

const routes: Route[] = [
    {path: '', redirectTo: '/articulos', pathMatch: 'full'},
    {path: 'articulos', component: ArticuloComponent},
    {path: 'areas', component: AreaComponent},
    {path: 'autores', component: AutorComponent},
    {path: 'usuarios', component: UsuarioComponent},
];

@NgModule({
    imports: [
        CommonModule,
        BrowserModule,
        RouterModule.forRoot(routes)
    ]
})
export class AppRoutingModule {
}
