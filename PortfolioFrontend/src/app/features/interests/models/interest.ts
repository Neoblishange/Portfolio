export interface Interest {
  id: number;
  description: string;
}

export type InterestPayload = Omit<Interest, 'id'>;
