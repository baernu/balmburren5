import {RoleDTO} from "./roleDTO";

export class UserDTO {
  id: string = "" ;
  version: string = "";
  username: string = "";
  password: string = "" ;
  firstname: string = "";
  lastname: string = "";
  roles: RoleDTO[]= [];
  updatedat: string = "";
  createdat: string = "";
  enabled: Boolean = false;

}
