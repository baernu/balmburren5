import {UserDTO} from "./userDTO";
import {DatesDTO} from "../../../admin/components/tour/service/DatesDTO";
import {ProductBindInfosDTO} from "../../../admin/components/product/service/ProductBindInfosDTO";
import {TourDTO} from "../../../admin/components/tour/service/TourDTO";

export class OrderDTO {
  id: string = "" ;
  version: string = "";
  deliverPeople: UserDTO = new UserDTO();
  date: DatesDTO = new DatesDTO();
  productBindInfos: ProductBindInfosDTO = new ProductBindInfosDTO();
  tour: TourDTO = new TourDTO();
  quantityOrdered: number = 0;
  quantityDelivered: number = 0;
  isChecked: Boolean = false;
  text: string = "";

}
