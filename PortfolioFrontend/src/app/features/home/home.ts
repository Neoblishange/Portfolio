import { AfterViewInit, Component, ElementRef, OnDestroy, inject, signal } from '@angular/core';
import { ExperiencesList } from '../experiences/experiences-list/experiences-list';
import { EducationsList } from '../educations/educations-list/educations-list';
import { ProjectsList } from '../projects/projects-list/projects-list';
import { SkillsList } from '../skills/skills-list/skills-list';
import { InterestsManager } from '../interests/interests-manager/interests-manager';
import { ProfileView } from '../profile/profile-view/profile-view';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [ProfileView, ExperiencesList, EducationsList, ProjectsList, SkillsList, InterestsManager],
  templateUrl: './home.html',
  styleUrl: './home.css'
})
export class Home implements AfterViewInit, OnDestroy {
  private readonly host = inject<ElementRef<HTMLElement>>(ElementRef);
  private sectionFrame?: number;

  protected readonly activeSection = signal('experiences');

  ngAfterViewInit(): void {
    window.addEventListener('scroll', this.onScroll, { passive: true });
    window.addEventListener('resize', this.onScroll);
    this.updateActiveSection();
  }

  ngOnDestroy(): void {
    window.removeEventListener('scroll', this.onScroll);
    window.removeEventListener('resize', this.onScroll);
    if (this.sectionFrame !== undefined) {
      window.cancelAnimationFrame(this.sectionFrame);
    }
  }

  private readonly onScroll = (): void => {
    if (this.sectionFrame !== undefined) {
      return;
    }

    this.sectionFrame = window.requestAnimationFrame(() => {
      this.sectionFrame = undefined;
      this.updateActiveSection();
    });
  };

  private updateActiveSection(): void {
    const sections = [...this.host.nativeElement.querySelectorAll<HTMLElement>('.section[id]')];
    if (sections.length === 0) {
      return;
    }

    const navigation = this.host.nativeElement.querySelector<HTMLElement>('.home-nav');
    const navigationHeight = navigation?.getBoundingClientRect().height ?? 0;
    const lastSection = sections[sections.length - 1];
    const lastBounds = lastSection.getBoundingClientRect();
    const trackingLine = navigationHeight + (window.innerHeight - navigationHeight) * 0.25;
    const lastSectionTrackingLine = navigationHeight + (window.innerHeight - navigationHeight) * 0.12;
    const atPageBottom = window.scrollY + window.innerHeight >=
      document.documentElement.scrollHeight - 2;

    if (
      (lastBounds.top <= lastSectionTrackingLine && lastBounds.bottom > 0) ||
      atPageBottom
    ) {
      this.activeSection.set(lastSection.id);
      return;
    }

    const currentSection = sections.find((section) => {
      const bounds = section.getBoundingClientRect();
      return bounds.top <= trackingLine && bounds.bottom > trackingLine;
    });

    if (currentSection) {
      this.activeSection.set(currentSection.id);
    }
  }
}
