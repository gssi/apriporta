export interface IRoom {
  id: number;
  roomName?: string | null;
  uid?: string | null;
}

export type NewRoom = Omit<IRoom, 'id'> & { id: null };
