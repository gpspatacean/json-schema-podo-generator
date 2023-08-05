import {ComponentFixture, TestBed} from '@angular/core/testing';

import {GeneratorsDropdownComponent} from './generators-dropdown.component';
import {HttpClientTestingModule} from "@angular/common/http/testing";

describe('GeneratorsDropdownComponent', () => {
    let component: GeneratorsDropdownComponent;
    let fixture: ComponentFixture<GeneratorsDropdownComponent>;

    beforeEach(() => {
        TestBed.configureTestingModule({
            declarations: [GeneratorsDropdownComponent],
            imports: [HttpClientTestingModule]
        });
        fixture = TestBed.createComponent(GeneratorsDropdownComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
