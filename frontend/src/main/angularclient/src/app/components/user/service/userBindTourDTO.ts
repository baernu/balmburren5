import {UserDTO} from "./userDTO";
import {TourDTO} from "../../../admin/components/tour/service/TourDTO";
import {DatesDTO} from "../../../admin/components/tour/service/DatesDTO";

export class UserBindTourDTO {
  id: string = "" ;
  version: string = "";
  person: UserDTO = new UserDTO() ;
  tour: TourDTO = new TourDTO() ;
  position: number = 0;
  startDate: DatesDTO | undefined;
  endDate: DatesDTO | undefined;

}
