import { Category } from './category';

export interface Skill {
  id: number;
  name: string;
  level: string | null;
  featured: boolean;
  category: Category;
  displayOrder: number;
}

export interface SkillPayload {
  name: string;
  level: string | null;
  categoryId: number;
  displayOrder: number;
}
