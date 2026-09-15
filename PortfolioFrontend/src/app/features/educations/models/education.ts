export interface Education {
  id: number;
  schoolName: string;
  location: string | null;
  startDate: string;
  endDate: string | null;
  degree: string;
  description: string | null;
}

export type EducationPayload = Omit<Education, 'id'>;
