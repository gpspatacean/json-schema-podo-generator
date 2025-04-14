import {Component} from '@angular/core';
import {GeneratorsService} from "./services/generators.service";

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    standalone: false
})
export class AppComponent {
  title = 'Plain Old Data Objects from JSON Schemas Generator';
  generatorSelected: boolean = false;

  constructor(private generatorsService: GeneratorsService) {
    this.generatorsService.targetGenerator.subscribe(value => {
      if (value !== "") {
        this.generatorSelected = true;
      }
    })
  }

  onClick() {
    this.generatorsService.createDownloadArchive().subscribe(
      (response: any) => {
        const fileName = response.headers.get('Content-Disposition')
          ?.split(';')[1]?.split('=')[1]?.replaceAll('"', '');
        const rawData = [];
        rawData.push(response.body);
        const a = document.createElement('a');
        a.download = (fileName !== undefined && fileName != "") ? fileName : "archive.zip";
        a.href = window.URL.createObjectURL(new Blob(rawData, {type: "application/octet-stream"}));
        a.click();
      }
    )
  }

  onFocusOut(event: FocusEvent) {
    const target = event.target as HTMLInputElement;
    if (target.value !== "") {
      this.generatorsService.generatorInput.options?.set("archiveName", target.value);
    }
  }
}
