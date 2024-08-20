import {UserDTO} from "./userDTO";
import {AddressDTO} from "./addressDTO";

export class UserBindDeliverAddressDTO {
  id: string = "" ;
  version: string = "";
  user: UserDTO = new UserDTO();
  number: string = "";
  address: AddressDTO = new AddressDTO();
}
