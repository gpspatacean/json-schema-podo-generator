import {TestBed} from '@angular/core/testing';
import {AppComponent} from './app.component';
import {GeneratorsDropdownComponent} from "./components/generators-dropdown/generators-dropdown.component";
import { provideHttpClientTesting } from "@angular/common/http/testing";
import {GeneratorsService} from "./services/generators.service";
import {SchemaTextareaComponent} from "./components/schema-textarea/schema-textarea.component";
import {GeneratorPropertiesComponent} from "./components/generator-properties/generator-properties.component";
import {FormsModule} from "@angular/forms";
import {AppConfigService} from "./services/app-config.service";
import {APP_BASE_HREF} from "@angular/common";
import { provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';

describe('AppComponent', () => {
  beforeEach(() => TestBed.configureTestingModule({
    declarations: [
        AppComponent,
        GeneratorsDropdownComponent,
        SchemaTextareaComponent,
        GeneratorPropertiesComponent
    ],
    imports: [FormsModule],
    providers: [
        GeneratorsService,
        AppConfigService,
        {
            provide: APP_BASE_HREF,
            useValue: "/"
        },
        provideHttpClient(withInterceptorsFromDi()),
        provideHttpClientTesting()
    ]
}));

  it('should create the app', () => {
    const fixture = TestBed.createComponent(AppComponent);
    fixture.detectChanges();
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });

  it(`should have as title 'Plain Old Data Objects from JSON Schemas Generator'`, () => {
    const fixture = TestBed.createComponent(AppComponent);
    fixture.detectChanges();
    const app = fixture.componentInstance;
    expect(app.title).toEqual('Plain Old Data Objects from JSON Schemas Generator');
  });

  it('should render title', () => {
    const fixture = TestBed.createComponent(AppComponent);
    fixture.detectChanges();
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled?.textContent).toContain('Plain Old Data Objects from JSON Schemas Generator');
  });
});
