import {Component, OnDestroy, OnInit} from '@angular/core';
import {GeneratorsService} from "../../services/generators.service";
import {Generator} from "../../models/generator";
import {Subscription} from "rxjs";
import {GeneratorProperty} from "../../models/generator-property";

@Component({
  selector: 'app-generators-dropdown',
  templateUrl: './generators-dropdown.component.html'
})
export class GeneratorsDropdownComponent implements OnInit, OnDestroy {
  constructor(private generatorsService: GeneratorsService) {
  }

  targetGenerator = '';
  generatorsList: Generator[] = [];
  sub!: Subscription;
  properties: GeneratorProperty[] = [];
  isFirstTime = true;

  ngOnInit(): void {
    this.sub = this.generatorsService.listGenerators().subscribe({
      next: generators => {
        this.generatorsList = generators;
        console.log(generators)
      },
      error: err => console.log(err),
      complete: () => console.log("done")
    })
  }

  ngOnDestroy(): void {
    this.sub.unsubscribe();
  }

  onSelectionChange(value: string) {
    if (this.isFirstTime) {
      this.isFirstTime = false;
    }
    this.targetGenerator = value;
    this.generatorsService.generatorInput.name = value;
  }
}
