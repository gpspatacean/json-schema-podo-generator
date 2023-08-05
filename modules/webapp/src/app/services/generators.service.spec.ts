import { TestBed } from '@angular/core/testing';

import { GeneratorsService } from './generators.service';
import {HttpClientTestingModule} from "@angular/common/http/testing";

describe('GeneratorsService', () => {
  let service: GeneratorsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[HttpClientTestingModule]
    });
    service = TestBed.inject(GeneratorsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should have listGenerators/getGeneratorProperties methods',()=>{
    expect(service.listGenerators).toBeTruthy();
    expect(service.getGeneratorProperties).toBeTruthy();
  });

});
