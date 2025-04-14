import {ComponentFixture, TestBed} from '@angular/core/testing';

import {SchemaTextareaComponent} from './schema-textarea.component';
import {provideHttpClientTesting} from "@angular/common/http/testing";
import {FormsModule} from "@angular/forms";
import {AppConfigService} from "../../services/app-config.service";
import {APP_BASE_HREF} from "@angular/common";
import {provideHttpClient, withInterceptorsFromDi} from '@angular/common/http';

describe('SchemaTextareaComponent', () => {
  let component: SchemaTextareaComponent;
  let fixture: ComponentFixture<SchemaTextareaComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SchemaTextareaComponent],
      imports: [FormsModule],
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
    fixture = TestBed.createComponent(SchemaTextareaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
