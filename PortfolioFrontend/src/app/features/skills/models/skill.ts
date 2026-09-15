import { Category } from './category';

export interface Skill {
  id: number;
  name: string;
  level: string | null;
  category: Category;
  displayOrder: number;
}

export interface SkillPayload {
  name: string;
  level: string | null;
  categoryId: number;
  displayOrder: number;
}
