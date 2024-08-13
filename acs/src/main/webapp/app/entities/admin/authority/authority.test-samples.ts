import { IAuthority, NewAuthority } from './authority.model';

export const sampleWithRequiredData: IAuthority = {
  name: 'fa09ca07-9d24-4137-8796-cf8a3fffb30a',
};

export const sampleWithPartialData: IAuthority = {
  name: 'b2c7ce0d-709a-43f3-a47a-802be7ce9565',
};

export const sampleWithFullData: IAuthority = {
  name: 'c122d0a6-89b9-4fd9-a9c0-4ac30767c6ef',
};

export const sampleWithNewData: NewAuthority = {
  name: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
