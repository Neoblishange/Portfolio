import { Component, inject } from '@angular/core';
import { rxResource } from '@angular/core/rxjs-interop';
import { ExperiencesList } from '../experiences/experiences-list/experiences-list';
import { EducationsList } from '../educations/educations-list/educations-list';
import { ProjectsList } from '../projects/projects-list/projects-list';
import { SkillsList } from '../skills/skills-list/skills-list';
import { InterestsManager } from '../interests/interests-manager/interests-manager';
import { ProfileApi } from '../profile/data-access/profile-api';
import { AVAILABILITY_LABELS } from '../profile/models/profile';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [ExperiencesList, EducationsList, ProjectsList, SkillsList, InterestsManager],
  templateUrl: './home.html',
  styleUrl: './home.css'
})
export class Home {
  private readonly profileApi = inject(ProfileApi);

  protected readonly availabilityLabels = AVAILABILITY_LABELS;

  protected readonly profileResource = rxResource({
    stream: () => this.profileApi.get()
  });
}
