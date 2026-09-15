import { ComponentFixture, TestBed } from '@angular/core/testing';
import { InterestsManager } from './interests-manager';

describe('InterestsManager', () => {
  let component: InterestsManager;
  let fixture: ComponentFixture<InterestsManager>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [InterestsManager],
    }).compileComponents();

    fixture = TestBed.createComponent(InterestsManager);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
