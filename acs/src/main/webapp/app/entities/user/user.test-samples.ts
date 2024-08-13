import { IUser } from './user.model';

export const sampleWithRequiredData: IUser = {
  id: 16330,
  login: 'Djo@VE9',
};

export const sampleWithPartialData: IUser = {
  id: 10042,
  login: 'u',
};

export const sampleWithFullData: IUser = {
  id: 14660,
  login: '-',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
