import {Component} from '@angular/core';

@Component({
    selector: 'app-schema-textarea',
    template: '<textarea class="form-control" rows="20" placeholder="{{schemaSample | json }}"></textarea>'
})
export class SchemaTextareaComponent {
    schemaSample: object = {"$schema": "https://json-schema.org/draft/2020-12/schema", "$id": "https://example.com/product.schema.json", "title": "Product", "description": "A product from Acme\'s catalog", "type": "object", "properties": {"productId": {"description": "The unique identifier for a product", "type": "integer"}, "productName": {"description": "Name of the product", "type": "string"}}};
}
