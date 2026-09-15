import { ComponentFixture, TestBed } from '@angular/core/testing';
import { EducationsDetail } from './educations-detail';

describe('EducationsDetail', () => {
  let component: EducationsDetail;
  let fixture: ComponentFixture<EducationsDetail>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EducationsDetail],
    }).compileComponents();

    fixture = TestBed.createComponent(EducationsDetail);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
