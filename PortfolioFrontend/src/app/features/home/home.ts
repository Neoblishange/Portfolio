import { AfterViewInit, Component, ElementRef, OnDestroy, inject, signal } from '@angular/core';
import { rxResource } from '@angular/core/rxjs-interop';
import { ExperiencesList } from '../experiences/experiences-list/experiences-list';
import { EducationsList } from '../educations/educations-list/educations-list';
import { ProjectsList } from '../projects/projects-list/projects-list';
import { SkillsList } from '../skills/skills-list/skills-list';
import { InterestsManager } from '../interests/interests-manager/interests-manager';
import { ProfileApi } from '../profile/data-access/profile-api';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [ExperiencesList, EducationsList, ProjectsList, SkillsList, InterestsManager],
  templateUrl: './home.html',
  styleUrl: './home.css'
})
export class Home implements AfterViewInit, OnDestroy {
  private readonly profileApi = inject(ProfileApi);
  private readonly host = inject<ElementRef<HTMLElement>>(ElementRef);
  private sectionObserver?: IntersectionObserver;
  private readonly visibleSections = new Set<Element>();

  protected readonly activeSection = signal('experiences');

  protected readonly profileResource = rxResource({
    stream: () => this.profileApi.get()
  });

  ngAfterViewInit(): void {
    if (typeof IntersectionObserver === 'undefined') {
      return;
    }

    this.sectionObserver = new IntersectionObserver(
      (entries) => {
        for (const entry of entries) {
          if (entry.isIntersecting) {
            this.visibleSections.add(entry.target);
          } else {
            this.visibleSections.delete(entry.target);
          }
        }

        const anchor = window.innerHeight * 0.25;
        const current = [...this.visibleSections]
          .sort((a, b) =>
            Math.abs(a.getBoundingClientRect().top - anchor) -
            Math.abs(b.getBoundingClientRect().top - anchor)
          )[0];

        if (current) {
          this.activeSection.set(current.id);
        }
      },
      { rootMargin: '-20% 0px -70% 0px', threshold: 0 }
    );

    this.host.nativeElement.querySelectorAll<HTMLElement>('.section[id]').forEach((section) => {
      this.sectionObserver?.observe(section);
    });
  }

  ngOnDestroy(): void {
    this.sectionObserver?.disconnect();
  }
}
