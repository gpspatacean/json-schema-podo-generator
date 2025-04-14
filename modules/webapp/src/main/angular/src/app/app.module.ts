import {APP_INITIALIZER, NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppComponent} from './app.component';
import {GeneratorsDropdownComponent} from './components/generators-dropdown/generators-dropdown.component';
import {FormsModule} from "@angular/forms";
import { provideHttpClient, withInterceptorsFromDi } from "@angular/common/http";
import {GeneratorPropertiesComponent} from './components/generator-properties/generator-properties.component';
import {SchemaTextareaComponent} from './components/schema-textarea/schema-textarea.component';
import {AppConfigService} from "./services/app-config.service";
import {APP_BASE_HREF} from "@angular/common";

const appInitializerFn = (appConfig: AppConfigService) => {
  return () => {
    return appConfig.loadAppConfiguration();
  };
};

@NgModule({ declarations: [
        AppComponent,
        GeneratorsDropdownComponent,
        GeneratorPropertiesComponent,
        SchemaTextareaComponent
    ],
    bootstrap: [AppComponent], imports: [BrowserModule,
        FormsModule], providers: [
        AppConfigService,
        {
            provide: APP_INITIALIZER,
            useFactory: appInitializerFn,
            multi: true,
            deps: [AppConfigService]
        },
        {
            provide: APP_BASE_HREF,
            useValue: '/' + (window.location.pathname.split('/')[1] || '')
        },
        provideHttpClient(withInterceptorsFromDi())
    ] })
export class AppModule {
}
