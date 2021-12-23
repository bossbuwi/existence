import { Role } from 'src/app/symphony/models/role';

export class User {
  username!: string;
  password!: string;
  token!: string;
  roles!: Role[];
}
