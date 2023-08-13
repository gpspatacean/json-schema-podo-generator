import {Injectable} from '@angular/core';
import {Generator} from "../models/generator";
import {HttpClient, HttpParams, HttpResponse} from "@angular/common/http";
import {Observable} from "rxjs";
import {GeneratorProperty} from "../models/generator-property";
import {GeneratorInput} from "../models/generator-input";

@Injectable({
  providedIn: 'root'
})
export class GeneratorsService {
  private backendLocation: string = "http://localhost:8080";
  public generatorInput: GeneratorInput = { name:'', payload: {}, options: new Map<string, string>()};

  constructor(private http: HttpClient) {
    console.log("service ctor");
  }

  listGenerators(): Observable<Generator[]> {
    return this.http.get<Generator[]>(this.backendLocation + "/generators");
  }

  getGeneratorProperties(targetGenerator: string): Observable<GeneratorProperty[]> {
    return this.http.get<GeneratorProperty[]>(this.backendLocation + "/generators/" + this.generatorInput.name + "/options");
  }

  createDownloadArchive(/*generatorInput: GeneratorInput*/) : Observable<Object> {
      const url: string = this.backendLocation + "/generators/" + this.generatorInput.name;
      let queryParams = new HttpParams();
      this.generatorInput.options
          ?.forEach((value,key) => queryParams = queryParams.append(key,value));

      return this.http.post(url,
        this.generatorInput.payload,
        {
          observe: 'response',
          responseType: 'arraybuffer',
          params: queryParams
        });
  }
}
