import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Title } from '@angular/platform-browser';
import { of } from 'rxjs';
import { ProfileApi } from '../data-access/profile-api';
import { ProfileView } from './profile-view';

describe('ProfileView', () => {
  let component: ProfileView;
  let fixture: ComponentFixture<ProfileView>;
  let title: Title;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProfileView],
      providers: [
        {
          provide: ProfileApi,
          useValue: {
            get: () => of({ firstName: 'Ada', lastName: 'Lovelace' }),
          },
        },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(ProfileView);
    component = fixture.componentInstance;
    title = TestBed.inject(Title);
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should set the document title to the profile name', () => {
    expect(title.getTitle()).toBe('Ada Lovelace');
  });
});
