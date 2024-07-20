import {DatesDTO} from "./DatesDTO";
import {TourDTO} from "./TourDTO";

export class TourBindDatesDTO {
  id: string = "" ;
  version: string = "";
  dates: DatesDTO = new DatesDTO();
  tour: TourDTO = new TourDTO();

}
