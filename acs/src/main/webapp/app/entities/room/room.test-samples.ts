import { IRoom, NewRoom } from './room.model';

export const sampleWithRequiredData: IRoom = {
  id: 19610,
};

export const sampleWithPartialData: IRoom = {
  id: 20980,
  uid: 'until shy',
};

export const sampleWithFullData: IRoom = {
  id: 8096,
  roomName: 'aw indolent wholly',
  uid: 'stump siege',
};

export const sampleWithNewData: NewRoom = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
