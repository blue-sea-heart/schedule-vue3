import { type IUser } from '@/shared/model/user.model';
import { type IScheduleStatus } from '@/shared/model/schedule-status.model';
import { type ICategory } from '@/shared/model/category.model';
import { type ITag } from '@/shared/model/tag.model';

import { type Priority } from '@/shared/model/enumerations/priority.model';
import { type Visibility } from '@/shared/model/enumerations/visibility.model';
export interface ISchedule {
  id?: number;
  title?: string;
  description?: string | null;
  location?: string | null;
  allDay?: boolean;
  startTime?: Date;
  endTime?: Date | null;
  priority?: keyof typeof Priority;
  completedAt?: Date | null;
  visibility?: keyof typeof Visibility;
  owner?: IUser | null;
  status?: IScheduleStatus | null;
  category?: ICategory | null;
  tags?: ITag[] | null;
}

export class Schedule implements ISchedule {
  constructor(
    public id?: number,
    public title?: string,
    public description?: string | null,
    public location?: string | null,
    public allDay?: boolean,
    public startTime?: Date,
    public endTime?: Date | null,
    public priority?: keyof typeof Priority,
    public completedAt?: Date | null,
    public visibility?: keyof typeof Visibility,
    public owner?: IUser | null,
    public status?: IScheduleStatus | null,
    public category?: ICategory | null,
    public tags?: ITag[] | null,
  ) {
    this.allDay = this.allDay ?? false;
  }
}
