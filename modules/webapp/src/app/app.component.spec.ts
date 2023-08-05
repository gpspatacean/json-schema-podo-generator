import { TestBed } from '@angular/core/testing';
import { AppComponent } from './app.component';
import {GeneratorsDropdownComponent} from "./components/generators-dropdown/generators-dropdown.component";
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {GeneratorsService} from "./services/generators.service";

describe('AppComponent', () => {
  beforeEach(() => TestBed.configureTestingModule({
    declarations: [
        AppComponent,
        GeneratorsDropdownComponent
    ],
    imports:[HttpClientTestingModule],
    providers:[GeneratorsService]
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
