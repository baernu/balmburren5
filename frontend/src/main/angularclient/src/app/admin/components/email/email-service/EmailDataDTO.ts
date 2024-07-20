export class EmailDataDTO {
  fromEmail: string = "" ;
  toEmail: string = "";
  subject: string = "";
  password: string = "";
  body: string = "";
  filename: string = "";
  file: File | undefined;
  byteArray: any[] | undefined;
  base64String: unknown;
}
