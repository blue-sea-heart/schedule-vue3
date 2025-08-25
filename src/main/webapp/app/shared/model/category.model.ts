export interface ICategory {
  id?: number;
  name?: string;
  color?: string | null;
}

export class Category implements ICategory {
  constructor(
    public id?: number,
    public name?: string,
    public color?: string | null,
  ) {}
}
