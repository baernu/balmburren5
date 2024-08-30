import {DatesDTO} from "./DatesDTO";
import {UserDTO} from "../../../../components/user/service/userDTO";

export class WorkDTO {
  id: string = "" ;
  version: string = "";
  user: UserDTO = new UserDTO();
  date: DatesDTO = new DatesDTO();
  startTime: string = "";
  endTime: string = "";
  workTime: string = "";
  description: string = "";
}
