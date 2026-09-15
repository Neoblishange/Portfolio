import { ComponentFixture, TestBed } from '@angular/core/testing';
import { SkillsDetail } from './skills-detail';

describe('SkillsDetail', () => {
  let component: SkillsDetail;
  let fixture: ComponentFixture<SkillsDetail>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SkillsDetail],
    }).compileComponents();

    fixture = TestBed.createComponent(SkillsDetail);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
