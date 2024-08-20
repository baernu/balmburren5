import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {EmailDataDTO} from "./EmailDataDTO";
import {InvoiceQRDTO} from "./invoiceQRDTO";
import {AndroidClientDTO} from "../../tour/service/androidClientDTO";

@Injectable({
  providedIn: 'root'
})
export class EmailService {
  private readonly baseUrl: string = 'http://localhost:8006/api/';
  // private readonly baseUrl: string = 'api/';
  private readonly emailNormalUrl: string;
  private readonly emailAttachmentUrl: string;
  private readonly getQRCodeUrl: string;
  private readonly sendTourDataUrl: string;
  private readonly retourTourDataUrl: string;


  constructor(private http: HttpClient) {
    this.emailNormalUrl = this.baseUrl + 'send/email/normal';
    this.emailAttachmentUrl = this.baseUrl + 'send/email/attachment';
    this.getQRCodeUrl = this.baseUrl + 'qrcode';
    this.sendTourDataUrl = this.baseUrl + 'send/email/tourdata';
    this.retourTourDataUrl = this.baseUrl + 'send/retour/tourdata';

  }

  public sendEmailNormal(email: EmailDataDTO):Observable<EmailDataDTO> {
    return this.http.post<EmailDataDTO>(this.emailNormalUrl, email,{withCredentials: true});}

  public sendEmailAttachment(email: EmailDataDTO):Observable<EmailDataDTO> {
    return this.http.post<EmailDataDTO>(this.emailAttachmentUrl, email,{withCredentials: true});}

  public createInvoiceQR(invoiceQR: InvoiceQRDTO):Observable<string> {
    return this.http.post<string>(this.getQRCodeUrl, invoiceQR,{withCredentials: true});}

  public sendTourData(androidClients: AndroidClientDTO[]):Observable<string> {
    return this.http.post<string>(this.sendTourDataUrl, androidClients,{withCredentials: true});}

  public retourTourData(json: string):Observable<AndroidClientDTO[]> {
    return this.http.post<AndroidClientDTO[]>(this.retourTourDataUrl, json,{withCredentials: true});}

}
