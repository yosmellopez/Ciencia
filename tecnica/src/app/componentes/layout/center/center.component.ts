import {Component, OnInit} from '@angular/core';
import {Location} from "@angular/common";
import {Router, RouterOutlet} from "@angular/router";
import {RouteInfo, Usuario} from "../../../modelo.datos";

declare function my_init_plugins();

const RUTAS: RouteInfo[] = [
    {
        path: '/usuarios',
        title: 'Lista de Usuarios',
        icon: 'group',
        class: 'waves-effect waves-cyan',
        authority: ["Admin"]
    },
    {
        path: '/areas',
        title: 'Lista de Areas',
        icon: 'directions_bus',
        class: 'waves-effect waves-cyan',
        authority: ["User"]
    },
    {
        path: '/autores',
        title: 'Lista de Autores',
        icon: 'place',
        class: 'waves-effect waves-cyan',
        authority: ["User"]
    },
    {
        path: '/articulos',
        title: 'Lista de Articulos',
        icon: 'directions',
        class: 'waves-effect waves-cyan',
        authority: ["User"]
    }
];

@Component({
    selector: 'app-center',
    templateUrl: './center.component.html',
    styleUrls: ['./center.component.css'],
    // animations: [routeAnimations]
})
export class CenterComponent implements OnInit {
    titulo: string;
    rutas: RouteInfo[] = RUTAS;
    usuario: Usuario = null;
    rol: Promise<String> = Promise.resolve("");
    rutasUsuario: RouteInfo[] = [];

    // routeAnimationsElements = ROUTE_ANIMATIONS_ELEMENTS;

    constructor(private router: Router, private location: Location) {
    }

    ngOnInit(): void {
        my_init_plugins();
        this.rutasUsuario = RUTAS;
        // this.animation.updateRouteAnimationType(true, true);
        // this.principal.identity().then(valor => {
        //     if (valor) {
        //         this.usuario = valor;
        //         this.rol = Promise.resolve(this.usuario.rol.name);
        //         this.rutas.forEach(ruta => {
        //             if (this.hasAuthority(ruta))
        //                 this.rutasUsuario.push(ruta);
        //         });
        //     }
        // });
    }

    getTitle() {
        var titlee = this.location.prepareExternalUrl(this.location.path());
        for (var item = 0; item < this.rutas.length; item++) {
            if (this.rutas[item].path === titlee) {
                this.titulo = this.rutas[item].title;
                return this.titulo;
            }
        }
        return 'Dashboard';
    }

    cerrarSession() {
        localStorage.removeItem("user_token");
        localStorage.removeItem("isAdmin");
        localStorage.removeItem("username");
        // this.principal.logout();
        this.router.navigate(["/login"]);
    }

    hasAuthority(menuItem: RouteInfo) {
        return menuItem.authority.includes(this.usuario.rol.nombre);
    }

    prepareRoute(outlet: RouterOutlet) {
        return outlet && outlet.activatedRouteData && outlet.activatedRouteData['animation'];
    }
}
