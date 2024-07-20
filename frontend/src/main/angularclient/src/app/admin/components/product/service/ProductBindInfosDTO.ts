import {ProductDTO} from "./ProductDTO";
import {DatesDTO} from "../../tour/service/DatesDTO";
import {ProductDetailsDTO} from "./ProductDetailsDTO";

export class ProductBindInfosDTO {
  id: string = "" ;
  version: string = "";
  product: ProductDTO = new ProductDTO();
  productDetails: ProductDetailsDTO = new ProductDetailsDTO();
  startDate: DatesDTO = new DatesDTO();
  endDate: DatesDTO = new DatesDTO();
  isChecked: Boolean = false;

}
