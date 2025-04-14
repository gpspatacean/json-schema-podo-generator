import {Component} from '@angular/core';
import {GeneratorProperty} from "../../models/generator-property";
import {GeneratorsService} from "../../services/generators.service";

@Component({
  selector: 'app-generator-properties',
  templateUrl: './generator-properties.component.html',
  standalone: false
})
export class GeneratorPropertiesComponent {
  properties: GeneratorProperty[] = [];

  constructor(private generatorsService: GeneratorsService) {
    this.generatorsService.targetGenerator.subscribe(value => {
      if (value !== "") {
        this.onGeneratorChanged(value);
      }
    });
  }

  onGeneratorChanged(newGenerator: string): void {
    this.generatorsService.generatorInput.name = newGenerator;
    this.generatorsService.getGeneratorProperties().subscribe({
      next: properties => {
        properties.forEach(genProp => genProp.name = this.normalizePropertyName(genProp.name));
        this.properties = properties;
      },
      error: err => console.log(err),
      complete: () => console.log("properties done")
    });
  }

  onPropertyChanged(event: Event) {
    const target = event.target as HTMLInputElement;
    this.generatorsService.generatorInput.options?.set(target.id, target.value);
  }

  private normalizePropertyName(originalPropName: string): string {
    const segments = originalPropName.split(';');
    return segments[segments.length - 1].replace(/^-+/, '');
  }
}
