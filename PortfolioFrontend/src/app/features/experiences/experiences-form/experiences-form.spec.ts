import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ExperiencesForm } from './experiences-form';

describe('ExperiencesForm', () => {
  let component: ExperiencesForm;
  let fixture: ComponentFixture<ExperiencesForm>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ExperiencesForm],
    }).compileComponents();

    fixture = TestBed.createComponent(ExperiencesForm);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
