import { type IUser } from '@/shared/model/user.model';

export interface IInAppNotification {
  id?: number;
  title?: string;
  content?: string | null;
  createdAt?: Date;
  read?: boolean;
  user?: IUser | null;
}

export class InAppNotification implements IInAppNotification {
  constructor(
    public id?: number,
    public title?: string,
    public content?: string | null,
    public createdAt?: Date,
    public read?: boolean,
    public user?: IUser | null,
  ) {
    this.read = this.read ?? false;
  }
}
