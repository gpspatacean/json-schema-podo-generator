import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SchemaTextareaComponent } from './schema-textarea.component';

describe('SchemaTextareaComponent', () => {
  let component: SchemaTextareaComponent;
  let fixture: ComponentFixture<SchemaTextareaComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SchemaTextareaComponent]
    });
    fixture = TestBed.createComponent(SchemaTextareaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
