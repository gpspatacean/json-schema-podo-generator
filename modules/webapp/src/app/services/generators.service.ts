import {Injectable} from '@angular/core';
import {Generator} from "../models/generator";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {GeneratorProperty} from "../models/generator-property";

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
}
