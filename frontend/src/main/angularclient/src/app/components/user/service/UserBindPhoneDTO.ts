import {UserDTO} from "./userDTO";

export class UserBindPhoneDTO {
  id: string = "" ;
  version: string = "";
  person: UserDTO = new UserDTO() ;
  email: string = "";
  phone: string = "";
  invoicePerson: UserDTO = new UserDTO();

}
