import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {EmailDataDTO} from "./EmailDataDTO";
import {InvoiceQRDTO} from "./invoiceQRDTO";
import {AndroidClientDTO} from "../../tour/service/androidClientDTO";
import {ByteDTO} from "../../../../components/user/service/byteDTO";

@Injectable({
  providedIn: 'root'
})
export class EmailService {
  private readonly baseUrl: string = 'http://localhost:8006/api/';
  // private readonly baseUrl: string = 'api/';
  private readonly emailUrl: string;
  private readonly getQRCodeUrl: string;
  private readonly sendTourDataUrl: string;
  private readonly retourTourDataUrl: string;
  private readonly backupUrl: string;


  constructor(private http: HttpClient) {
    this.emailUrl = this.baseUrl + 'em/send/email/';
    this.getQRCodeUrl = this.baseUrl + 'em/qrcode';
    this.sendTourDataUrl = this.baseUrl + 'em/send/email/tourdata';
    this.retourTourDataUrl = this.baseUrl + 'em/send/retour/tourdata';
    this.backupUrl = this.baseUrl + 'em/backup/';

  }

  public sendEmail(email: EmailDataDTO):Observable<EmailDataDTO> {
    return this.http.post<EmailDataDTO>(this.emailUrl, email,{withCredentials: true});}

  public sendRegisterEmail(email: string):Observable<string> {
    return this.http.get<string>(this.emailUrl + 'register/'+ email,{withCredentials: true});}

  public createInvoiceQR(invoiceQR: InvoiceQRDTO):Observable<string> {
    return this.http.post<string>(this.getQRCodeUrl, invoiceQR,{withCredentials: true});}

  public sendTourData(androidClients: AndroidClientDTO[]):Observable<string> {
    return this.http.post<string>(this.sendTourDataUrl, androidClients,{withCredentials: true});}

  public retourTourData(json: string):Observable<AndroidClientDTO[]> {
    return this.http.post<AndroidClientDTO[]>(this.retourTourDataUrl, json,{withCredentials: true});}

  public backupSend():Observable<string> {
    return this.http.get<string>(this.backupUrl + 'send/',{withCredentials: true});}

  public backupWriteToFile():Observable<string> {
    return this.http.get<string>(this.backupUrl + 'tofile/',{withCredentials: true});}

  public backupImport(byteDTO: ByteDTO):Observable<ByteDTO> {
    return this.http.patch<ByteDTO>(this.backupUrl + 'import/', byteDTO,{withCredentials: true});}

  public backupImportMigrate():Observable<string> {
    return this.http.get<string>(this.backupUrl + 'import/migrate/',{withCredentials: true});}


}
