import { IEmployee, NewEmployee } from './employee.model';

export const sampleWithRequiredData: IEmployee = {
  id: 24247,
};

export const sampleWithPartialData: IEmployee = {
  id: 21441,
  firstName: 'Gino',
  email: 'Berneice_Ledner51@gmail.com',
};

export const sampleWithFullData: IEmployee = {
  id: 6506,
  firstName: 'Lonnie',
  lastName: 'Yost',
  email: 'Precious.Rohan@gmail.com',
};

export const sampleWithNewData: NewEmployee = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
