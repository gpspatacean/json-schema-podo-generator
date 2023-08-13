import {Component} from '@angular/core';
import {GeneratorsService} from "./services/generators.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html'
})
export class AppComponent {
  constructor(private generatorsService: GeneratorsService) {
  }

  title = 'Plain Old Data Objects from JSON Schemas Generator';

  onClick() {
    console.log("Button Clicked");
    this.generatorsService.createDownloadArchive().subscribe(
      (response: any) => {
        const fileName = response.headers.get('Content-Disposition')
          ?.split(';')[1].split('=')[1];
        const rawData = [];
        rawData.push(response.body);
        const a = document.createElement('a');
        //TODO: Handle name retrieval from http Content-Disposition header
        a.download = "test.zip";
        a.href = window.URL.createObjectURL(new Blob(rawData, {type: "application/octet-stream"}));
        a.click();
      }
    )
  }
}
