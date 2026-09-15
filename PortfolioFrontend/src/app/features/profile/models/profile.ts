export type Availability = 'AVAILABLE' | 'NOT_AVAILABLE' | 'OPEN_TO_OPPORTUNITIES';

export const AVAILABILITY_LABELS: Record<Availability, string> = {
  AVAILABLE: 'Available',
  NOT_AVAILABLE: 'Not available',
  OPEN_TO_OPPORTUNITIES: 'Open to work'
};

export interface Profile {
  id: number;
  firstName: string;
  lastName: string;
  jobTitle: string;
  phone: string | null;
  email: string;
  linkedinUrl: string | null;
  githubUrl: string | null;
  pitch: string;
  yearsOfExperience: number;
  availability: Availability;
}

export type ProfilePayload = Omit<Profile, 'id'>;
