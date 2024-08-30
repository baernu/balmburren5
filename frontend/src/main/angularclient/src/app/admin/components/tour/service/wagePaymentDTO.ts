import {DatesDTO} from "./DatesDTO";
import {UserDTO} from "../../../../components/user/service/userDTO";
import {InvoiceDTO} from "../../../../components/user/service/invoiceDTO";

export class WagePaymentDTO {
  id: string = "" ;
  version: string = "";
  user: UserDTO = new UserDTO();
  invoice: InvoiceDTO = new InvoiceDTO();
  dateFrom: DatesDTO = new DatesDTO();
  dateTo: DatesDTO = new DatesDTO();
}
