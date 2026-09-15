import { Component, inject } from '@angular/core';
import { rxResource, toSignal } from '@angular/core/rxjs-interop';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { map } from 'rxjs';
import { SkillsApi } from '../data-access/skills-api';

@Component({
  selector: 'app-skills-detail',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './skills-detail.html',
  styleUrl: './skills-detail.css'
})
export class SkillsDetail {
  private readonly route = inject(ActivatedRoute);
  private readonly skillsApi = inject(SkillsApi);

  private readonly id = toSignal(
    this.route.paramMap.pipe(map(params => Number(params.get('id'))))
  );

  protected readonly skillResource = rxResource({
    params: () => this.id(),
    stream: ({ params: id }) => this.skillsApi.getById(id)
  });
}
