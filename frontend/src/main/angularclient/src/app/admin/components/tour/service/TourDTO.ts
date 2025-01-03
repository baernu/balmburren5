import {DatesDTO} from "./DatesDTO";
import {UserDTO} from "../../../../components/user/service/userDTO";

export class TourDTO {
  id: string = "" ;
  version: string = "";
  number: string = "0";
  user: UserDTO = new UserDTO();
  dates: DatesDTO[] = [];

}
