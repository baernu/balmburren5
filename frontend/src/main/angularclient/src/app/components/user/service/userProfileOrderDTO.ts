import {UserDTO} from "./userDTO";
import {ProductBindInfosDTO} from "../../../admin/components/product/service/ProductBindInfosDTO";
import {TourDTO} from "../../../admin/components/tour/service/TourDTO";

export class UserProfileOrderDTO {
  id: string = "" ;
  version: string = "";
  person: UserDTO = new UserDTO();
  productBindProductDetails: ProductBindInfosDTO = new ProductBindInfosDTO();
  tour: TourDTO = new TourDTO();
  firstWeekOrder: number = 0;
  secondWeekOrder: number = 0;

}
