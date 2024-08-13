import { ITag, NewTag } from './tag.model';

export const sampleWithRequiredData: ITag = {
  id: 21405,
};

export const sampleWithPartialData: ITag = {
  id: 32596,
  code: 'which',
};

export const sampleWithFullData: ITag = {
  id: 22950,
  code: 'honour',
};

export const sampleWithNewData: NewTag = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
