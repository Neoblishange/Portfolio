import { ComponentFixture, TestBed } from '@angular/core/testing';
import { EducationsForm } from './educations-form';

describe('EducationsForm', () => {
  let component: EducationsForm;
  let fixture: ComponentFixture<EducationsForm>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EducationsForm],
    }).compileComponents();

    fixture = TestBed.createComponent(EducationsForm);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
