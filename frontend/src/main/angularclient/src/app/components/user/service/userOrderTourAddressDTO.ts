import {AddressDTO} from "./addressDTO";
import {OrderDTO} from "./orderDTO";
import {UserBindTourDTO} from "./userBindTourDTO";

export class UserOrderTourAddressDTO{

  address: AddressDTO = new AddressDTO();
  order: OrderDTO = new OrderDTO();
  userbindtour: UserBindTourDTO = new UserBindTourDTO();
}
