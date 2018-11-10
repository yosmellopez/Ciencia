import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";
import {BrowserModule} from "@angular/platform-browser";
import {Route, RouterModule} from "@angular/router";
import {ArticuloComponent} from "./componentes/articulo/articulo.component";
import {AreaComponent} from "./componentes/area/area.component";
import {AutorComponent} from "./componentes/autor/autor.component";

const routes: Route[] = [
    {path: '', redirectTo: '/inicio', pathMatch: 'full'},
    {path: 'inicio', component: ArticuloComponent},
    {path: 'area', component: AreaComponent},
    {path: 'autor', component: AutorComponent},
    // {
    //   path: '',
    //   component: AdminComponent,
    //   canActivate: [AdminGuard],
    //   children: [{path: 'admin', loadChildren: './admin/admin.module#AdminModule'}]
    // },
    // {
    //   path: '',
    //   canActivate: [UsuarioGuard],
    //   component: ComunComponent,
    //   children: [{path: '', loadChildren: './comun/comun.module#ComunModule'}]
    // }
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
