import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {TourDTO} from "./TourDTO";
import {DatesDTO} from "./DatesDTO";
import {TourBindDatesDTO} from "./TourBindDatesDTO";
import {TourDateBindInfosDTO} from "./TourDateBindInfosDTO";
import {ProductDTO} from "../../product/service/ProductDTO";
import {ProductDetailsDTO} from "../../product/service/ProductDetailsDTO";
import { WorkDTO } from './workDTO';
import {WagePaymentDTO} from "./wagePaymentDTO";


@Injectable({
  providedIn: 'root'
})
export class TourServiceService {
  private readonly tourUrl: string;
  private readonly datesUrl: string;
  private readonly tourBindDatesUrl: string;
  private readonly tourDateBindInfosUrl: string;
  private readonly workUrl: string;
  private readonly workPayment: string;
  private readonly baseUrl: string = 'http://localhost:8006/api/';
  // private readonly baseUrl: string = 'api/';
  constructor(private http: HttpClient) {
    this.tourUrl = this.baseUrl +'tr/tour/';
    this.datesUrl = this.baseUrl + 'tr/dates/';
    this.tourBindDatesUrl = this.baseUrl + 'tr/tour/bind/dates/';
    this.tourDateBindInfosUrl = this.baseUrl + 'tr/tour/bind/dates/product/infos/';
    this.workUrl = this.baseUrl + 'wr/work/';
    this.workPayment = this.baseUrl + 'wr/work/wage/payment/';

  }

  public createWagePayment(wagePayment: WagePaymentDTO):Observable<WagePaymentDTO> {
    return this.http.post<WagePaymentDTO>(this.workPayment, wagePayment,{withCredentials: true});}

  public putWagePayment(wagePayment: WagePaymentDTO):Observable<WagePaymentDTO> {
    return this.http.post<WagePaymentDTO>(this.workPayment, wagePayment,{withCredentials: true});}

  public getWagePayment(username: string, date: DatesDTO):Observable<WagePaymentDTO> {
    return this.http.get<WagePaymentDTO>(this.workPayment + username + '/' + date.id, {withCredentials: true});}

  public deleteWagePayment(username: string, date: DatesDTO):Observable<WagePaymentDTO> {
    return this.http.delete<WagePaymentDTO>(this.workPayment + username + '/' + date.id, {withCredentials: true});}

  public getAllWagePaymentsForUser(username: string):Observable<WagePaymentDTO[]> {
    return this.http.get<WagePaymentDTO[]>(this.workPayment + username, {withCredentials: true});}

  public getAllWagePaymentsForUserandIntervall(username: string, startDate: DatesDTO, endDate: DatesDTO):Observable<WagePaymentDTO[]> {
    return this.http.get<WagePaymentDTO[]>(this.workPayment + username + '/' + startDate.id + '/' + endDate.id, {withCredentials: true});}
  public createWork(work: WorkDTO):Observable<WorkDTO> {
    return this.http.post<WorkDTO>(this.workUrl, work,{withCredentials: true});}

  public putWork(work: WorkDTO):Observable<WorkDTO> {
    return this.http.put<WorkDTO>(this.workUrl, work,{withCredentials: true});}

  public getWork(username: string, date: DatesDTO):Observable<WorkDTO> {
    return this.http.get<WorkDTO>(this.workUrl + username + '/' + date.id, {withCredentials: true});}

  public deleteWork(username: string, date: DatesDTO):Observable<WorkDTO> {
    return this.http.delete<WorkDTO>(this.workUrl + username + '/' + date.id, {withCredentials: true});}

  public deleteWorkById(work: WorkDTO):Observable<WorkDTO> {
    return this.http.patch<WorkDTO>(this.workUrl, work,{withCredentials: true});}

  public getAllWorksForUser(username: string):Observable<WorkDTO[]> {
    return this.http.get<WorkDTO[]>(this.workUrl + username, {withCredentials: true});}

  public getAllWorksForUserandIntervall(username: string, startDate: DatesDTO, endDate: DatesDTO):Observable<WorkDTO[]> {
    return this.http.get<WorkDTO[]>(this.workUrl + username + '/' + startDate.id + '/' + endDate.id, {withCredentials: true});}
  public getTour(number: string):Observable<TourDTO> {
    return this.http.get<TourDTO>(this.tourUrl + number, {withCredentials: true});}

  public getTours():Observable<TourDTO[]>{
    return this.http.get<TourDTO[]>(this.tourUrl, {withCredentials: true});}

  public createTour(tour: TourDTO):Observable<TourDTO> {
    return this.http.post<TourDTO>(this.tourUrl, tour,{withCredentials: true});}

  public putTour(tour: TourDTO):Observable<TourDTO> {
    return this.http.put<TourDTO>(this.tourUrl, tour,{withCredentials: true});}

  public getDates(date: DatesDTO):Observable<DatesDTO> {
    return this.http.get<DatesDTO>(this.datesUrl + date.id,{withCredentials: true});}

  public existDates(date: DatesDTO):Observable<Boolean> {
    return this.http.get<Boolean>(this.datesUrl + "exist/" + date.date,{withCredentials: true});}

  public createDates(dates: DatesDTO):Observable<DatesDTO> {
    return this.http.post<DatesDTO>(this.datesUrl, dates, {withCredentials: true});}

  public createTourBindDates(tourBindDate: TourBindDatesDTO):Observable<TourBindDatesDTO> {
    return this.http.post<TourBindDatesDTO>(this.tourBindDatesUrl, tourBindDate, {withCredentials: true});}

  public getTourBindDates(number: string, date: string):Observable<TourBindDatesDTO> {
    return this.http.get<TourBindDatesDTO>(this.tourBindDatesUrl + number + '/' + date,{withCredentials: true});}

  public deleteTourBindDates(number: string, date: string):Observable<string> {
    return this.http.delete<string>(this.tourBindDatesUrl + number + '/' + date,{withCredentials: true});}

  public getAllTourBindDatesForTour(number: string):Observable<TourBindDatesDTO[]> {
    return this.http.get<TourBindDatesDTO[]>(this.tourBindDatesUrl + number,{withCredentials: true});}

  public getTourDatesBindInfos(tour: TourDTO, date: DatesDTO, product: ProductDTO, detail: ProductDetailsDTO):Observable<TourDateBindInfosDTO> {
    return this.http.get<TourDateBindInfosDTO>(this.tourDateBindInfosUrl + tour.number + '/' + date.id + '/' + product.name + '/' + detail.id, {withCredentials: true});}

  public deleteTourDatesBindInfos(tour: TourDTO, date: DatesDTO, product: ProductDTO, detail: ProductDetailsDTO):Observable<TourDateBindInfosDTO> {
    return this.http.delete<TourDateBindInfosDTO>(this.tourDateBindInfosUrl + tour.number + '/' + date.id + '/' + product.name + '/' + detail.id, {withCredentials: true});}

  public createTourDateBindInfos(tourDateBindInfos: TourDateBindInfosDTO):Observable<TourDateBindInfosDTO> {
    return this.http.post<TourDateBindInfosDTO>(this.tourDateBindInfosUrl, tourDateBindInfos, {withCredentials: true});}

  public putTourDateBindInfos(tourDateBindInfos: TourDateBindInfosDTO):Observable<TourDateBindInfosDTO> {
    return this.http.put<TourDateBindInfosDTO>(this.tourDateBindInfosUrl, tourDateBindInfos, {withCredentials: true});}

  public getAllTourDatesBindInfosForTourAndDate(tour: TourDTO, date: DatesDTO):Observable<TourDateBindInfosDTO[]> {
    return this.http.get<TourDateBindInfosDTO[]>(this.tourDateBindInfosUrl + tour.number + '/' + date.id, {withCredentials: true});}

  public getAllTourDatesBindInfosForTourAndDateBetween(tour: TourDTO, startDate: DatesDTO, endDate: DatesDTO): Observable<TourDateBindInfosDTO[]> {
    return this.http.get<TourDateBindInfosDTO[]>(this.tourDateBindInfosUrl + 'between/' + tour.number + '/' + startDate.id + '/' + endDate.id, {withCredentials : true});}

  public getAllTourDatesBindInfosForTour(tour: TourDTO):Observable<TourDateBindInfosDTO[]> {
    return this.http.get<TourDateBindInfosDTO[]>(this.tourDateBindInfosUrl + tour.number, {withCredentials: true});}

  public getAllTourDatesBindInfos():Observable<TourDateBindInfosDTO[]> {
    return this.http.get<TourDateBindInfosDTO[]>(this.tourDateBindInfosUrl,{withCredentials: true});}

  public existTourDatesBindInfos(tour: TourDTO, date: DatesDTO, product: ProductDTO, detail: ProductDetailsDTO):Observable<Boolean> {
    return this.http.get<Boolean>(this.tourDateBindInfosUrl + 'exist/' + tour.number + '/' + date.id + '/' + product.name + '/' + detail.id, {withCredentials: true});}
}
