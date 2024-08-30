import {DatesDTO} from "./DatesDTO";

export class WorkDTO {
  id: string = "" ;
  version: string = "";
  number: string = "0";
  dates: DatesDTO[] = [];
  startTime: string = "";
  endTime: string = "";
  description: string = "";
}
