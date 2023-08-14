import {ComponentFixture, TestBed} from '@angular/core/testing';

import {GeneratorsDropdownComponent} from './generators-dropdown.component';
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {GeneratorPropertiesComponent} from "../generator-properties/generator-properties.component";

describe('GeneratorsDropdownComponent', () => {
  let component: GeneratorsDropdownComponent;
  let fixture: ComponentFixture<GeneratorsDropdownComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [
        GeneratorsDropdownComponent,
        GeneratorPropertiesComponent
      ],
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
