import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ExperiencesDetail } from './experiences-detail';

describe('ExperiencesDetail', () => {
  let component: ExperiencesDetail;
  let fixture: ComponentFixture<ExperiencesDetail>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ExperiencesDetail],
    }).compileComponents();

    fixture = TestBed.createComponent(ExperiencesDetail);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
