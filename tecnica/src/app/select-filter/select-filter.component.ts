import {Component, Input, OnInit} from '@angular/core';
import {FormControl} from "@angular/forms";

@Component({
    selector: 'app-select-filter',
    templateUrl: './select-filter.component.html',
    styleUrls: ['./select-filter.component.css']
})
export class SelectFilterComponent implements OnInit {
    @Input() control: FormControl;

    constructor() {
    }

    ngOnInit() {
    }

    filtrarSelect(event: Event) {
        console.log(this.control)
    }
}
