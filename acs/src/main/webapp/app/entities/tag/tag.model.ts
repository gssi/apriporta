import { IEmployee } from 'app/entities/employee/employee.model';

export interface ITag {
  id: number;
  code?: string | null;
  employee?: IEmployee | null;
}

export type NewTag = Omit<ITag, 'id'> & { id: null };
