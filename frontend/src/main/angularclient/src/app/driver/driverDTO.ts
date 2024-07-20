import {UserDTO} from "../components/user/service/userDTO";
import {DatesDTO} from "../admin/components/tour/service/DatesDTO";
import {ProductBindInfosDTO} from "../admin/components/product/service/ProductBindInfosDTO";
import {TourDTO} from "../admin/components/tour/service/TourDTO";
import {UserBindPhoneDTO} from "../components/user/service/UserBindPhoneDTO";
import {UserBindDeliverAddressDTO} from "../components/user/service/userBindDeliverAddressDTO";

export class DriverDTO {
  id: string = "" ;
  version: string = "";
  deliverPeople: UserDTO = new UserDTO();
  date: DatesDTO = new DatesDTO();
  productBindInfos: ProductBindInfosDTO = new ProductBindInfosDTO();
  tour: TourDTO = new TourDTO();
  quantityOrdered: number = 0;
  quantityDelivered: number = 0;
  isChecked: Boolean = false;
  userBindPhone: UserBindPhoneDTO = new UserBindPhoneDTO();
  userBindAddress: UserBindDeliverAddressDTO = new UserBindDeliverAddressDTO();
}
