import {TestBed} from '@angular/core/testing';
import {AppConfigService} from './app-config.service';
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {APP_BASE_HREF} from "@angular/common";

describe('AppConfigService', () => {
  let service: AppConfigService;

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
    service = TestBed.inject(AppConfigService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
