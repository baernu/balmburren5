export class InvoiceDTO {
  id: string = "" ;
  version: string = "";
  amount: number = 0;
  paid: number = 0;
  isPaid: Boolean = false;
  isSent: Boolean = false;
}
