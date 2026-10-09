export interface Interest {
  id: number;
  name: string;
  description: string;
}

export type InterestPayload = Omit<Interest, 'id'>;
