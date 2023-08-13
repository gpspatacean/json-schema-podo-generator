import {ComponentFixture, TestBed} from '@angular/core/testing';

import {SchemaTextareaComponent} from './schema-textarea.component';
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {FormsModule} from "@angular/forms";

describe('SchemaTextareaComponent', () => {
  let component: SchemaTextareaComponent;
  let fixture: ComponentFixture<SchemaTextareaComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SchemaTextareaComponent],
      imports: [HttpClientTestingModule, FormsModule]
    });
    fixture = TestBed.createComponent(SchemaTextareaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
