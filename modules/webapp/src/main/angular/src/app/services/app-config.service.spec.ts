import {TestBed} from '@angular/core/testing';
import {AppConfigService} from './app-config.service';
import {provideHttpClientTesting} from "@angular/common/http/testing";
import {APP_BASE_HREF} from "@angular/common";
import {provideHttpClient, withInterceptorsFromDi} from '@angular/common/http';

describe('AppConfigService', () => {
  let service: AppConfigService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [],
      providers: [
        AppConfigService,
        {
          provide: APP_BASE_HREF,
          useValue: "/"
        },
        provideHttpClient(withInterceptorsFromDi()),
        provideHttpClientTesting()
      ]
    });
    service = TestBed.inject(AppConfigService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
