import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppComponent} from './app.component';
import {AppRoutingModule} from "./app.routing";
import {UsuarioComponent} from './componentes/usuario/usuario.component';
import {AreaComponent} from './componentes/area/area.component';
import {ArticuloComponent} from './componentes/articulo/articulo.component';
import {AutorComponent} from './componentes/autor/autor.component';
import {RouterModule} from "@angular/router";
import {HeaderComponent} from './componentes/layout/header/header.component';
import {FooterComponent} from './componentes/layout/footer/footer.component';
import {AreaWindow} from './componentes/area/area-window/area-window.component';
import {MensajeModule} from "./mensaje/mensaje.module";
import {AngularMaterialModule} from "./services/material.module";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {ReactiveFormsModule} from "@angular/forms";

@NgModule({
    declarations: [
        AppComponent,
        UsuarioComponent,
        AreaComponent,
        ArticuloComponent,
        AutorComponent,
        HeaderComponent,
        FooterComponent,
        AreaWindow
    ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        RouterModule,
        MensajeModule,
        AngularMaterialModule,
        BrowserAnimationsModule,
        ReactiveFormsModule,
    ],
    entryComponents: [AreaWindow],
    providers: [],
    bootstrap: [AppComponent]
})
export class AppModule {
}
