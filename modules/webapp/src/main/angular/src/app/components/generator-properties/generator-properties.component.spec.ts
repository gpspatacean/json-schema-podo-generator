import {ComponentFixture, TestBed} from '@angular/core/testing';

import {GeneratorPropertiesComponent} from './generator-properties.component';
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {AppConfigService} from "../../services/app-config.service";

describe('GeneratorPropertiesComponent', () => {
  let component: GeneratorPropertiesComponent;
  let fixture: ComponentFixture<GeneratorPropertiesComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [GeneratorPropertiesComponent],
      imports: [HttpClientTestingModule],
      providers: [AppConfigService]
    });
    fixture = TestBed.createComponent(GeneratorPropertiesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
