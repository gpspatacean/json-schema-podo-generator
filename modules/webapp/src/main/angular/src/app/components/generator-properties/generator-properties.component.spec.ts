import {ComponentFixture, TestBed} from '@angular/core/testing';

import {GeneratorPropertiesComponent} from './generator-properties.component';
import {provideHttpClientTesting} from "@angular/common/http/testing";
import {AppConfigService} from "../../services/app-config.service";
import {APP_BASE_HREF} from "@angular/common";
import {provideHttpClient, withInterceptorsFromDi} from '@angular/common/http';

describe('GeneratorPropertiesComponent', () => {
  let component: GeneratorPropertiesComponent;
  let fixture: ComponentFixture<GeneratorPropertiesComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [GeneratorPropertiesComponent],
      imports: [],
      providers: [
        AppConfigService,
        {
          provide: APP_BASE_HREF,
          useValue: "/"
        },
        provideHttpClient(withInterceptorsFromDi()),
        provideHttpClientTesting()
      ]
    });
    fixture = TestBed.createComponent(GeneratorPropertiesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
