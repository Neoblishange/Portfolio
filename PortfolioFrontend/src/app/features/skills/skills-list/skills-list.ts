import { Component, computed, inject } from '@angular/core';
import { rxResource } from '@angular/core/rxjs-interop';
import { SkillsApi } from '../data-access/skills-api';
import { Skill } from '../models/skill';
import { Category } from '../models/category';

type CategoryImage =
  | 'architecture_design'
  | 'database'
  | 'languages'
  | 'languages_frameworks'
  | 'methods_collaboration'
  | 'tools_devops';

interface SkillsGroup {
  category: Category;
  skills: Skill[];
}

@Component({
  selector: 'app-skills-list',
  standalone: true,
  templateUrl: './skills-list.html',
  styleUrl: './skills-list.css'
})
export class SkillsList {
  private readonly skillsApi = inject(SkillsApi);

  protected readonly skillsResource = rxResource({
    stream: () => this.skillsApi.getAll()
  });

  protected readonly groupedSkills = computed<SkillsGroup[]>(() => {
    const skills = (this.skillsResource.value() ?? []).filter(skill => skill.featured);
    const groups = new Map<number, SkillsGroup>();

    for (const skill of skills) {
      const existing = groups.get(skill.category.id);
      if (existing) {
        existing.skills.push(skill);
      } else {
        groups.set(skill.category.id, { category: skill.category, skills: [skill] });
      }
    }

    return Array.from(groups.values())
      .sort((a, b) => a.category.name.localeCompare(b.category.name))
      .map(group => ({
        ...group,
        skills: group.skills.sort((a, b) =>
          a.displayOrder - b.displayOrder || a.name.localeCompare(b.name)
        )
      }));
  });

  protected categoryImage(categoryName: string): CategoryImage {
    const name = categoryName.toLocaleLowerCase().normalize('NFD').replace(/[\u0300-\u036f]/g, '');

    if (/database|base de donnees|data|sql/.test(name)) return 'database';
    if (/devops|cloud|infra|outil|tool/.test(name)) return 'tools_devops';
    if (/method|methode|collab|agile/.test(name)) return 'methods_collaboration';
    if (/architecture|design|ux|graph/.test(name)) return 'architecture_design';
    if (/framework|frontend|front-end|backend|back-end|web|api|serveur/.test(name)) {
      return 'languages_frameworks';
    }
    if (/language|langue|programming|programmation/.test(name)) return 'languages';
    return 'languages_frameworks';
  }

  protected onDelete(id: number): void {
    if (!confirm('Delete this skill?')) {
      return;
    }

    this.skillsApi.delete(id).subscribe({
      next: () => this.skillsResource.reload()
    });
  }
}
