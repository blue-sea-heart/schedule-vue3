export interface IScheduleStatus {
  id?: number;
  code?: string;
  name?: string;
  color?: string | null;
  sortNo?: number;
  isDefault?: boolean;
  isTerminal?: boolean;
}

export class ScheduleStatus implements IScheduleStatus {
  constructor(
    public id?: number,
    public code?: string,
    public name?: string,
    public color?: string | null,
    public sortNo?: number,
    public isDefault?: boolean,
    public isTerminal?: boolean,
  ) {
    this.isDefault = this.isDefault ?? false;
    this.isTerminal = this.isTerminal ?? false;
  }
}
