import { Component, computed, inject } from '@angular/core';
import { rxResource } from '@angular/core/rxjs-interop';
import { RouterLink } from '@angular/router';
import { SkillsApi } from '../data-access/skills-api';
import { Skill } from '../models/skill';
import { Category } from '../models/category';

interface SkillsGroup {
  category: Category;
  skills: Skill[];
}

@Component({
  selector: 'app-skills-list',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './skills-list.html',
  styleUrl: './skills-list.css'
})
export class SkillsList {
  private readonly skillsApi = inject(SkillsApi);

  protected readonly skillsResource = rxResource({
    stream: () => this.skillsApi.getAll()
  });

  protected readonly groupedSkills = computed<SkillsGroup[]>(() => {
    const skills = this.skillsResource.value() ?? [];
    const groups = new Map<number, SkillsGroup>();

    for (const skill of skills) {
      const existing = groups.get(skill.category.id);
      if (existing) {
        existing.skills.push(skill);
      } else {
        groups.set(skill.category.id, { category: skill.category, skills: [skill] });
      }
    }

    return Array.from(groups.values()).sort((a, b) =>
      a.category.name.localeCompare(b.category.name)
    );
  });

  protected onDelete(id: number): void {
    if (!confirm('Supprimer ce skill ?')) {
      return;
    }

    this.skillsApi.delete(id).subscribe({
      next: () => this.skillsResource.reload()
    });
  }
}
