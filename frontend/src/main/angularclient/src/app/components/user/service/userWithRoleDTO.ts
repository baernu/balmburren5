import {RoleDTO} from "./roleDTO";

export class UserWithRoleDTO {
  id: string = "" ;
  version: string = "";
  username: string = "";
  password: string = "" ;
  firstname: string = "";
  lastname: string = "";
  adminIsChecked: boolean = false;
  driverIsChecked: boolean = false;
  userIsChecked: boolean = false;
  userKathyIsChecked: boolean = false;
  kathyIsChecked: boolean = false;
  superadminIsChecked: boolean = false;
  roles: RoleDTO[]= [];
  updatedat: string = "";
  createdat: string = "";
  enabled: Boolean = false;
}
