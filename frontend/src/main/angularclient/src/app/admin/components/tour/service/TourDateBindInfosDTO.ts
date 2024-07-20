import {DatesDTO} from "./DatesDTO";
import {ProductBindInfosDTO} from "../../product/service/ProductBindInfosDTO";
import {TourDTO} from "./TourDTO";

export class TourDateBindInfosDTO {
  id: string = "" ;
  version: string = "";
  tour: TourDTO = new TourDTO();
  dates: DatesDTO = new DatesDTO();
  productBindInfos: ProductBindInfosDTO = new ProductBindInfosDTO();

}
