import { type IUser } from '@/shared/model/user.model';

import { type ViewType } from '@/shared/model/enumerations/view-type.model';
import { type WeekStart } from '@/shared/model/enumerations/week-start.model';
export interface IViewPreference {
  id?: number;
  defaultView?: keyof typeof ViewType;
  lastStart?: Date | null;
  lastEnd?: Date | null;
  weekStart?: keyof typeof WeekStart;
  showWeekend?: boolean;
  user?: IUser | null;
}

export class ViewPreference implements IViewPreference {
  constructor(
    public id?: number,
    public defaultView?: keyof typeof ViewType,
    public lastStart?: Date | null,
    public lastEnd?: Date | null,
    public weekStart?: keyof typeof WeekStart,
    public showWeekend?: boolean,
    public user?: IUser | null,
  ) {
    this.showWeekend = this.showWeekend ?? false;
  }
}
