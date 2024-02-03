import {TestBed} from '@angular/core/testing';

import {GeneratorsService} from './generators.service';
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {AppConfigService} from "./app-config.service";
import {APP_BASE_HREF} from "@angular/common";

describe('GeneratorsService', () => {
  let service: GeneratorsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        AppConfigService,
        {
          provide: APP_BASE_HREF,
          useValue: "/"
        }
      ]
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
