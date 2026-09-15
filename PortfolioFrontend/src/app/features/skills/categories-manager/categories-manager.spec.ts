import { ComponentFixture, TestBed } from '@angular/core/testing';
import { CategoriesManager } from './categories-manager';

describe('CategoriesManager', () => {
  let component: CategoriesManager;
  let fixture: ComponentFixture<CategoriesManager>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CategoriesManager],
    }).compileComponents();

    fixture = TestBed.createComponent(CategoriesManager);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
