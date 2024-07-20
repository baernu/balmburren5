import {UserDTO} from "../../../../components/user/service/userDTO";

export class EmailUserDTO {
  person: UserDTO = new UserDTO() ;
  email: string = "";
  phone: string = "";
  isChecked: Boolean = false;
}
