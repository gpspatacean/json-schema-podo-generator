import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {AppConfig} from "../models/app-config";
import {tap} from "rxjs";

@Injectable()
export class AppConfigService {
    private appConfig: AppConfig | undefined;

    constructor(private http: HttpClient) {
    }

    loadAppConfiguration() {
        console.log("Loading App Configuration");
        return this.http
            .get<AppConfig>('/assets/config.json')
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
