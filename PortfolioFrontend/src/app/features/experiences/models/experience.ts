export interface Experience {
  id: number;
  company: string;
  position: string;
  location: string | null;
  description: string;
  startDate: string;
  endDate: string | null;
  current: boolean;
}

export type ExperiencePayload = Omit<Experience, 'id'>;
