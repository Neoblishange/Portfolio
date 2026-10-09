import {Skill} from '../../skills/models/skill';

export interface Project {
  id: number;
  title: string;
  slug: string;
  projectType: string;
  projectContext: string;
  shortDescription: string;
  description: string;
  functionalities: string[];
  startDate: string;
  endDate: string | null;
  skills: Skill[];
}

export type ProjectPayload = Omit<Project, 'id'>;
