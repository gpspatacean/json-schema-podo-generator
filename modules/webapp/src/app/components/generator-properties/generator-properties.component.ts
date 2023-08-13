import {Component, Input, OnChanges, OnDestroy, OnInit, SimpleChanges} from '@angular/core';
import {GeneratorProperty} from "../../models/generator-property";
import {GeneratorsService} from "../../services/generators.service";

@Component({
  selector: 'app-generator-properties',
  templateUrl: './generator-properties.component.html'
})
export class GeneratorPropertiesComponent implements OnChanges, OnInit, OnDestroy {
  constructor(private generatorsService: GeneratorsService) {
  }

  @Input() targetGenerator: string = "";
  properties: GeneratorProperty[] = [];

  ngOnChanges(changes: SimpleChanges): void {
    console.log(changes);
    if (changes['targetGenerator'].currentValue !== "") {
      this.generatorsService.getGeneratorProperties(this.targetGenerator).subscribe({
        next: properties => {
          properties.forEach(genProp =>  genProp.name = this.normalizePropertyName(genProp.name));
          this.properties = properties;
        },
        error: err => console.log(err),
        complete: () => console.log("properties done")
      });
    }
  }

  private normalizePropertyName(originalPropName: string) : string {
    const segments = originalPropName.split(';');
    return segments[segments.length-1].replace(/^-+/,'');
  }

    ngOnDestroy(): void {
        console.log("gen props ondestroy()")
    }

    ngOnInit(): void {
      console.log("gen props oninit()")
    }

  onPropertyChanged(event: Event) {
    console.log(event);
    const target = event.target as HTMLInputElement;
    this.generatorsService.generatorInput.options?.set(target.id, target.value);
  }
}
