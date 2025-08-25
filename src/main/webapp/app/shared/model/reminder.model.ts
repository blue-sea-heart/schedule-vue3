import { type ISchedule } from '@/shared/model/schedule.model';

import { type ReminderChannel } from '@/shared/model/enumerations/reminder-channel.model';
export interface IReminder {
  id?: number;
  remindAt?: Date;
  channel?: keyof typeof ReminderChannel;
  subject?: string | null;
  content?: string | null;
  sent?: boolean;
  attemptCount?: number | null;
  lastAttemptAt?: Date | null;
  lastErrorMsg?: string | null;
  errorMsg?: string | null;
  schedule?: ISchedule | null;
}

export class Reminder implements IReminder {
  constructor(
    public id?: number,
    public remindAt?: Date,
    public channel?: keyof typeof ReminderChannel,
    public subject?: string | null,
    public content?: string | null,
    public sent?: boolean,
    public attemptCount?: number | null,
    public lastAttemptAt?: Date | null,
    public lastErrorMsg?: string | null,
    public errorMsg?: string | null,
    public schedule?: ISchedule | null,
  ) {
    this.sent = this.sent ?? false;
  }
}
