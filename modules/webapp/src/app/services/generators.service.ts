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

  constructor(private http: HttpClient) {
    console.log("service ctor");
  }

  listGenerators(): Observable<Generator[]> {
    return this.http.get<Generator[]>(this.backendLocation + "/generators");
  }

  getGeneratorProperties(targetGenerator: string): Observable<GeneratorProperty[]> {
    return this.http.get<GeneratorProperty[]>(this.backendLocation + "/generators/" + targetGenerator + "/options");
  }

  createDownloadArchive(generatorInput: GeneratorInput) : Observable<Object> {
      const url: string = this.backendLocation + "/generators/" + generatorInput.name;
      let queryParams = new HttpParams();
      generatorInput.options
          ?.forEach((k,v) => queryParams = queryParams.append(k,v));

      return this.http.post(url,
        generatorInput.payload,
        {
          observe: 'response',
          responseType: 'arraybuffer',
          params: queryParams
        })
  }
}
