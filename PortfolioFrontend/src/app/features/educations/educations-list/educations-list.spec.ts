import { ComponentFixture, TestBed } from '@angular/core/testing';
import { EducationsList } from './educations-list';

describe('EducationsList', () => {
  let component: EducationsList;
  let fixture: ComponentFixture<EducationsList>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EducationsList],
    }).compileComponents();

    fixture = TestBed.createComponent(EducationsList);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
