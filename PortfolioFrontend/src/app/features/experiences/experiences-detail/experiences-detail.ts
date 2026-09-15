import { Component, inject } from '@angular/core';
import { rxResource, toSignal } from '@angular/core/rxjs-interop';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { map } from 'rxjs';
import { ExperiencesApi } from '../data-access/experiences-api';

@Component({
  selector: 'app-experiences-detail',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './experiences-detail.html',
  styleUrl: './experiences-detail.css'
})
export class ExperiencesDetail {
  private readonly route = inject(ActivatedRoute);
  private readonly experiencesApi = inject(ExperiencesApi);

  private readonly id = toSignal(
    this.route.paramMap.pipe(map(params => Number(params.get('id'))))
  );

  protected readonly experienceResource = rxResource({
    params: () => this.id(),
    stream: ({ params: id }) => this.experiencesApi.getById(id)
  });
}
