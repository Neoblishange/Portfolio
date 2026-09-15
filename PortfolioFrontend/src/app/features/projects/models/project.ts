export interface Project {
  id: number;
  title: string;
  slug: string;
  shortDescription: string;
  description: string;
  startDate: string;
  endDate: string | null;
}

export type ProjectPayload = Omit<Project, 'id'>;
