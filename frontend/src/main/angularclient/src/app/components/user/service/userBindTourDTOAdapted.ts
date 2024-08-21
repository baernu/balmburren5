import {UserDTO} from "./userDTO";
import {TourDTO} from "../../../admin/components/tour/service/TourDTO";
import {DatesDTO} from "../../../admin/components/tour/service/DatesDTO";

export class UserBindTourDTOAdapted {
  id: string = "" ;
  version: string = "";
  user: UserDTO = new UserDTO() ;
  tour: TourDTO = new TourDTO() ;
  position: number = 0;
  startDate: string | undefined;
  endDate: string | undefined;

}
