import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppComponent} from './app.component';
import {GeneratorsDropdownComponent} from './components/generators-dropdown/generators-dropdown.component';
import {FormsModule} from "@angular/forms";
import {HttpClientModule} from "@angular/common/http";
import { GeneratorPropertiesComponent } from './components/generator-properties/generator-properties.component';
import { SchemaTextareaComponent } from './components/schema-textarea/schema-textarea.component';

@NgModule({
  declarations: [
    AppComponent,
    GeneratorsDropdownComponent,
    GeneratorPropertiesComponent,
    SchemaTextareaComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
