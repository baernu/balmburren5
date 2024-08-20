import {UserDTO} from "./userDTO";

export class UserBindPhoneDTO {
  id: string = "" ;
  version: string = "";
  user: UserDTO = new UserDTO() ;
  email: string = "";
  phone: string = "";
  invoicePerson: UserDTO = new UserDTO();

}
