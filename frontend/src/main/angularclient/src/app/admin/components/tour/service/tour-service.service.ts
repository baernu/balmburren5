import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {TourDTO} from "./TourDTO";
import {DatesDTO} from "./DatesDTO";
import {TourBindDatesDTO} from "./TourBindDatesDTO";
import {TourDateBindInfosDTO} from "./TourDateBindInfosDTO";
import {ProductDTO} from "../../product/service/ProductDTO";
import {ProductDetailsDTO} from "../../product/service/ProductDetailsDTO";


@Injectable({
  providedIn: 'root'
})
export class TourServiceService {
  private readonly tourUrl: string;
  private readonly datesUrl: string;
  private readonly tourBindDatesUrl: string;
  private readonly tourDateBindInfosUrl: string;
  // private readonly baseUrl: string = 'http://localhost:8080/api/';
  private readonly baseUrl: string = 'api/';
  constructor(private http: HttpClient) {
    this.tourUrl = this.baseUrl +'tour/';
    this.datesUrl = this.baseUrl + 'dates/';
    this.tourBindDatesUrl = this.baseUrl + 'tour/bind/dates/';
    this.tourDateBindInfosUrl = this.baseUrl + 'tour/bind/dates/product/infos/';

  }

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
