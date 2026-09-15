import { TestBed } from '@angular/core/testing';
import { InterestsApi } from './interests-api';

describe('InterestsApi', () => {
  let service: InterestsApi;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(InterestsApi);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
