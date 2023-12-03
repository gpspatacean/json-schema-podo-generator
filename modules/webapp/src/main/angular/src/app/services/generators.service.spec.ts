import {TestBed} from '@angular/core/testing';

import {GeneratorsService} from './generators.service';
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {AppConfigService} from "./app-config.service";

describe('GeneratorsService', () => {
  let service: GeneratorsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [AppConfigService]
    });
    service = TestBed.inject(GeneratorsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should have listGenerators/getGeneratorProperties methods', () => {
    expect(service.listGenerators).toBeTruthy();
    expect(service.getGeneratorProperties).toBeTruthy();
  });

});
