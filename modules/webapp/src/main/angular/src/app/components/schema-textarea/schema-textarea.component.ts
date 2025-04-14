import {Component} from '@angular/core';
import {GeneratorsService} from "../../services/generators.service";

@Component({
  selector: 'app-schema-textarea',
  template: `<textarea class="form-control"
                       rows="20"
                       [(ngModel)]="schemaInput"
                       (ngModelChange)="updatePayload()"></textarea>`,
  standalone: false
})
export class SchemaTextareaComponent {
  constructor(private generatorsService: GeneratorsService) {
    this.generatorsService.generatorInput.payload = this.schemaSample;
  }

  schemaSample: object = {
    "$schema": "https://json-schema.org/draft/2020-12/schema",
    "$id": "https://example.com/product.schema.json",
    "title": "Product",
    "description": "A product from Acme's catalog",
    "type": "object",
    "properties": {
      "productId": {"description": "The unique identifier for a product", "type": "integer"},
      "productName": {"description": "Name of the product", "type": "string"}
    }
  };
  schemaInput: string = JSON.stringify(this.schemaSample, undefined, 4);

  updatePayload() {
    this.generatorsService.generatorInput.payload = JSON.parse(this.schemaInput);
  }
}
