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
        next: properties => this.properties = properties,
        error: err => console.log(err),
        complete: () => console.log("properties done")
      });
    }
  }

    ngOnDestroy(): void {
        console.log("gen props ondestroy()")
    }

    ngOnInit(): void {
      console.log("gen props oninit()")
    }
}
