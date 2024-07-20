import {UserDTO} from "./userDTO";
import {RoleDTO} from "./roleDTO";

export class UserBindRoleDTO {
  id: string = "" ;
  version: string = "";
  person: UserDTO = new UserDTO() ;
  role: RoleDTO = new RoleDTO() ;

}
