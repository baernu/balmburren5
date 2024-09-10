import {UserDTO} from "./userDTO";
import {InvoiceDTO} from "./invoiceDTO";
import {DatesDTO} from "../../../admin/components/tour/service/DatesDTO";

export class DriverBindInvoiceDTO {
  id: string = "" ;
  version: string = "";
  personInvoice: UserDTO = new UserDTO();
  invoice: InvoiceDTO = new InvoiceDTO();
  dateFrom: DatesDTO = new DatesDTO();
  dateTo: DatesDTO = new DatesDTO();
  isChecked: Boolean = false;
}
