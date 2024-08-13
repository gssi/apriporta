import dayjs from 'dayjs/esm';

import { IAccessRule, NewAccessRule } from './access-rule.model';

export const sampleWithRequiredData: IAccessRule = {
  id: 16998,
};

export const sampleWithPartialData: IAccessRule = {
  id: 10886,
  startDate: dayjs('2024-08-08T08:23'),
};

export const sampleWithFullData: IAccessRule = {
  id: 17376,
  startDate: dayjs('2024-08-08T06:46'),
  endDate: dayjs('2024-08-07T19:08'),
  notes: 'differentiate inwardly freak',
};

export const sampleWithNewData: NewAccessRule = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
