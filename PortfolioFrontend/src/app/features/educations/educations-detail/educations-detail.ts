import { Component, inject } from '@angular/core';
import { rxResource, toSignal } from '@angular/core/rxjs-interop';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { map } from 'rxjs';
import { EducationsApi } from '../data-access/educations-api';

@Component({
  selector: 'app-educations-detail',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './educations-detail.html',
  styleUrl: './educations-detail.css'
})
export class EducationsDetail {
  private readonly route = inject(ActivatedRoute);
  private readonly educationsApi = inject(EducationsApi);

  private readonly id = toSignal(
    this.route.paramMap.pipe(map(params => Number(params.get('id'))))
  );

  protected readonly educationResource = rxResource({
    params: () => this.id(),
    stream: ({ params: id }) => this.educationsApi.getById(id)
  });
}
