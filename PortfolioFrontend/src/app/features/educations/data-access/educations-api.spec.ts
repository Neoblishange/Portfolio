import { TestBed } from '@angular/core/testing';
import { EducationsApi } from './educations-api';

describe('EducationsApi', () => {
  let service: EducationsApi;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EducationsApi);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
