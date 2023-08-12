import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GeneratorPropertiesComponent } from './generator-properties.component';

describe('GeneratorPropertiesComponent', () => {
  let component: GeneratorPropertiesComponent;
  let fixture: ComponentFixture<GeneratorPropertiesComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [GeneratorPropertiesComponent]
    });
    fixture = TestBed.createComponent(GeneratorPropertiesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
