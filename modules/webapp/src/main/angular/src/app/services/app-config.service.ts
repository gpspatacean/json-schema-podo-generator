import {Inject, Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {AppConfig} from "../models/app-config";
import {tap} from "rxjs";
import {APP_BASE_HREF} from "@angular/common";

@Injectable()
export class AppConfigService {

  private appConfig: AppConfig | undefined;

  constructor(private http: HttpClient,
              @Inject(APP_BASE_HREF) private baseHref: string) {
  }

  loadAppConfiguration() {
    console.log("Loading App Configuration");

    const baseConfigPath: string = "/assets/config.json";
    const configPath: string = this.baseHref === "/" ? baseConfigPath : this.baseHref + baseConfigPath;
    return this.http
      .get<AppConfig>(configPath)
      .pipe(tap(data => {
          this.appConfig = data;
          console.log("Data read:" + data);
        })
      );
  }

  getConfig() {
    return this.appConfig;
  }
}
