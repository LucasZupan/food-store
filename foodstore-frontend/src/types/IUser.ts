import type { Rol } from "./Rol";

export interface IUser {
  email: string;
  loggedIn: boolean;
  password: string;
  role: Rol;
}
