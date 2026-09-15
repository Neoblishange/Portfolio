import { TestBed } from '@angular/core/testing';
import { SkillsApi } from './skills-api';

describe('SkillsApi', () => {
  let service: SkillsApi;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SkillsApi);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
