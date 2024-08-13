import dayjs from 'dayjs/esm';
import { IEmployee } from 'app/entities/employee/employee.model';
import { IRoom } from 'app/entities/room/room.model';

export interface IAccessRule {
  id: number;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  notes?: string | null;
  employee?: IEmployee | null;
  room?: IRoom | null;
}

export type NewAccessRule = Omit<IAccessRule, 'id'> & { id: null };
