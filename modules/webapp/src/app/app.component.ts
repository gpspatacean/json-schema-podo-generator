import {Component} from '@angular/core';
import {GeneratorsService} from "./services/generators.service";
import {GeneratorInput} from "./models/generator-input";

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
        const payload: object = {"$schema": "https://json-schema.org/draft/2020-12/schema", "$id": "https://example.com/product.schema.json", "title": "Product", "description": "A product from Acme\'s catalog", "type": "object", "properties": {"productId": {"description": "The unique identifier for a product", "type": "integer"}, "productName": {"description": "Name of the product", "type": "string"}}};
        const options = new Map<string, string>();
        options.set("package", "generated.apis");
        options.set("version", "1.0.0");
        const generatorInput:GeneratorInput = { name: 'java', payload: payload, options: options };
        this.generatorsService.createDownloadArchive(/*generatorInput*/).subscribe(
            (response: any) => {
                let fileName = response.headers.get('Content-Disposition')
                    ?.split(';')[1].split('=')[1];
                let rawData = [];
                rawData.push(response.body);
                let a = document.createElement('a');
                //TODO: Handle name retrieval from http Content-Disposition header
                a.download = "test.zip";
                a.href = window.URL.createObjectURL(new Blob(rawData, {type: "application/octet-stream"}));
                a.click();
            }
        )
    }
}
