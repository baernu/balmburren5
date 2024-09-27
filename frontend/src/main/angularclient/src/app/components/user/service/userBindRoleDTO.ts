import {UserDTO} from "./userDTO";
import {RoleDTO} from "./roleDTO";

export class UserBindRoleDTO {
  id: string = "" ;
  version: string = "";
  user: UserDTO = new UserDTO() ;
  role: RoleDTO = new RoleDTO() ;

}
