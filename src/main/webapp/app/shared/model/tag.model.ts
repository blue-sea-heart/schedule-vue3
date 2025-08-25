import { type ISchedule } from '@/shared/model/schedule.model';

export interface ITag {
  id?: number;
  name?: string;
  color?: string | null;
  schedules?: ISchedule[] | null;
}

export class Tag implements ITag {
  constructor(
    public id?: number,
    public name?: string,
    public color?: string | null,
    public schedules?: ISchedule[] | null,
  ) {}
}
