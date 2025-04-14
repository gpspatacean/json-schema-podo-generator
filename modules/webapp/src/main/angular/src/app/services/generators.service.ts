import {Injectable} from '@angular/core';
import {Generator} from "../models/generator";
import { HttpClient, HttpParams } from "@angular/common/http";
import {BehaviorSubject, Observable} from "rxjs";
import {GeneratorProperty} from "../models/generator-property";
import {GeneratorInput} from "../models/generator-input";
import {AppConfigService} from "./app-config.service";

@Injectable({
    providedIn: 'root'
})
export class GeneratorsService {
    public generatorInput: GeneratorInput = {name: '', payload: {}, options: new Map<string, string>()};
    private readonly backendLocation: string | undefined = "http://localhost:8080";
    private targetGeneratorSubject = new BehaviorSubject('');
    public targetGenerator = this.targetGeneratorSubject.asObservable();

    constructor(private http: HttpClient,
                private appConfigService: AppConfigService) {
        this.backendLocation = appConfigService.getConfig()?.backendLocation;
    }

    updateTargetGenerator(value: string) {
        this.targetGeneratorSubject.next(value);
    }

    listGenerators(): Observable<Generator[]> {
        return this.http.get<Generator[]>(this.backendLocation + "/generators");
    }

    getGeneratorProperties(): Observable<GeneratorProperty[]> {
        return this.http.get<GeneratorProperty[]>(this.backendLocation + "/generators/" + this.generatorInput.name + "/options");
    }

    createDownloadArchive(): Observable<object> {
        const url: string = this.backendLocation + "/generators/" + this.generatorInput.name;
        let queryParams = new HttpParams();
        this.generatorInput.options
            ?.forEach((value, key) => queryParams = queryParams.append(key, value));

        return this.http.post(url,
            this.generatorInput.payload,
            {
                observe: 'response',
                responseType: 'arraybuffer',
                params: queryParams
            });
    }
}
