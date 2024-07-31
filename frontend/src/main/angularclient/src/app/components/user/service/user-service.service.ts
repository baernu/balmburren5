import { Injectable } from '@angular/core';
import { HttpClient} from '@angular/common/http';
import { UserDTO } from './userDTO';



import { Observable } from 'rxjs';
import {AuthenticateDTO} from "./authenticateDTO";
import {RoleDTO} from "./roleDTO";
import {UserBindRoleDTO} from "./userBindRoleDTO";
import {UserBindTourDTO} from "./userBindTourDTO";
import {OrderDTO} from "./orderDTO";
import {ProductDTO} from "../../../admin/components/product/service/ProductDTO";
import {ProductDetailsDTO} from "../../../admin/components/product/service/ProductDetailsDTO";
import {DatesDTO} from "../../../admin/components/tour/service/DatesDTO";
import {UserProfileOrderDTO} from "./userProfileOrderDTO";
import {TourDTO} from "../../../admin/components/tour/service/TourDTO";
import {InvoiceDTO} from "./invoiceDTO";
import {UserBindInvoiceDTO} from "./userBindInvoiceDTO";
import {UserBindPhoneDTO} from "./UserBindPhoneDTO";
import {AddressDTO} from "./addressDTO";
import {UserBindDeliverAddressDTO} from "./userBindDeliverAddressDTO";
import {ReferenceDTO} from "./ReferenceDTO";

@Injectable()
export class UserService {
  private readonly authUrl: string;
  private readonly usersUrl: string;
  private readonly getUserUrl: string;
  private readonly authenticateUrl: string;
  private readonly setCookieUrl: string;
  private readonly deleteCookieUrl: string;
  private readonly readCookieUrl: string;
  private readonly isAdminUrl: string;
  private readonly username: string = "";
  private readonly getRolesForPersonUrl: string;
  private readonly getAllRoles: string;
  private readonly bindUserTourUrl: string;
  private readonly orderUrl: string;
  private readonly userProfileOrderUrl: string;
  private readonly invoiceUrl: string;
  private readonly userBindInvoiceUrl: string;
  private readonly userBindPhoneUrl: string;
  private readonly addressUrl: string;
  private readonly userBindAddress: string;
  private readonly isBasicUrl: string;
  private readonly isDriverUrl: string;
  private readonly isKathyUrl: string;
  private readonly isUserKathyUrl: string;
  private readonly baseUrl: string = 'http://localhost:8006/api/';

  // private readonly baseUrl: string = 'api/';


  constructor(private http: HttpClient) {
    this.usersUrl = this.baseUrl + 'users/';
    this.authenticateUrl = this.baseUrl +'authenticate';
    this.setCookieUrl =  this.baseUrl + 'set-cookie/';
    this.deleteCookieUrl = this.baseUrl + 'delete-cookie';
    this.getUserUrl = this.baseUrl + 'users/';
    this.authUrl = this.baseUrl + 'auth/';
    this.readCookieUrl = this.baseUrl + 'read-cookie';
    this.isAdminUrl =  this.baseUrl + 'is_admin/';
    this.isBasicUrl =  this.baseUrl + 'is_basic/';
    this.isDriverUrl = this.baseUrl + 'is_driver/';
    this.isKathyUrl = this.baseUrl + 'is_kathy/';
    this.isUserKathyUrl = this.baseUrl + 'is_user_kathy/';
    this.getRolesForPersonUrl = this.baseUrl + 'person/bind/role/';
    this.getAllRoles = this.baseUrl + 'role';
    this.bindUserTourUrl = this.baseUrl + 'person/bind/tour/';
    this.orderUrl = this.baseUrl + 'order/';
    this.userProfileOrderUrl = this.baseUrl + 'order/person/profile/';
    this.invoiceUrl = this.baseUrl + 'invoice/';
    this.userBindInvoiceUrl = this.baseUrl + 'person/bind/invoice/';
    this.userBindPhoneUrl = this.baseUrl + 'person/bind/phone/';
    this.userBindAddress = this.baseUrl + 'person/bind/deliveraddress';
    this.addressUrl = this.baseUrl + 'address';



  }

  public findUser(username: string):Observable<UserDTO> {
    return this.http.get<UserDTO>(this.usersUrl + username, {withCredentials : true});}

  public findUserById(id: number):Observable<UserDTO> {
    return this.http.get<UserDTO>(this.usersUrl + 'byid/' +id, {withCredentials : true});}

  public existUser(username: string):Observable<UserDTO> {
    return this.http.get<UserDTO>(this.authUrl + 'exist/' + username, {withCredentials : true});}

  public findAll(): Observable<UserDTO[]> {
    return this.http.get<UserDTO[]>(this.usersUrl,{withCredentials : true});}

  public save(user: UserDTO): Observable<UserDTO>{
    return this.http.post<UserDTO>(this.authUrl + 'register', user, {withCredentials : true});}

  public putUser(user: UserDTO): Observable<UserDTO>{
    return this.http.put<UserDTO>(this.usersUrl, user, {withCredentials : true});}

  public login(user: UserDTO):Observable<string>  {
    return this.http.post<string>(this.authUrl + 'login', user, {withCredentials : true});}

  public authenticate(authenticate: AuthenticateDTO): Observable<string> {
    return this.http.post<string>(this.authenticateUrl, authenticate,{withCredentials : true});}

  public setTokenCookie(username: string): Observable<string> {
    console.log("in the function of setTokenCookie...");
    return this.http.get<string>(this.setCookieUrl + username,{withCredentials : true});}

  public deleteTokenCookie(): Observable<string> {
    return this.http.get<string>(this.deleteCookieUrl ,{withCredentials : true} );}

  public isAdmin(username: string | null): Observable<Boolean>{
    return this.http.get<Boolean>(this.isAdminUrl + username, {withCredentials: true});}

  public isBasic(username: string | null): Observable<Boolean>{
    return this.http.get<Boolean>(this.isBasicUrl + username, {withCredentials: true});}

  public isUserKathy(username: string | null): Observable<Boolean>{
    return this.http.get<Boolean>(this.isUserKathyUrl + username, {withCredentials: true});}

  public isDriver(username: string | null): Observable<Boolean>{
    return this.http.get<Boolean>(this.isDriverUrl + username, {withCredentials: true});}

  public isKathy(username: string | null): Observable<Boolean>{
    return this.http.get<Boolean>(this.isKathyUrl + username, {withCredentials: true});}

  public findAllRolesForPerson(username: string): Observable<UserBindRoleDTO[]> {
    return this.http.get<UserBindRoleDTO[]>(this.getRolesForPersonUrl + username,{withCredentials : true});}

  public createPersonBindRole(userBindRole: UserBindRoleDTO): Observable<UserBindRoleDTO> {
    return this.http.post<UserBindRoleDTO>(this.getRolesForPersonUrl,userBindRole, {withCredentials : true});}

  public findAllRoles(): Observable<RoleDTO[]> {
    return this.http.get<RoleDTO[]>(this.getAllRoles,{withCredentials : true});}

  public getPersonBindRole(username: string, role: string): Observable<UserBindRoleDTO> {
    return this.http.get<UserBindRoleDTO>(this.getRolesForPersonUrl + username + '/' + role, {withCredentials : true});}

  public deletePersonBindRole(username: string, role: string): Observable<UserBindRoleDTO> {
    return this.http.delete<UserBindRoleDTO>(this.getRolesForPersonUrl + username + '/' + role, {withCredentials : true});}

  public addPersonBindTour(personBindTour: UserBindTourDTO): Observable<UserBindTourDTO> {
    return this.http.post<UserBindTourDTO>(this.bindUserTourUrl, personBindTour, {withCredentials : true});}

  public updatePersonBindTour(personBindTour: UserBindTourDTO): Observable<UserBindTourDTO> {
    return this.http.put<UserBindTourDTO>(this.bindUserTourUrl, personBindTour, {withCredentials : true});}

  public getPersonBindTour(username: string, tour: string): Observable<UserBindTourDTO> {
    return this.http.get<UserBindTourDTO>(this.bindUserTourUrl + username + '/' + tour, {withCredentials : true});}

  public deletePersonBindTour(username: string, tour: string): Observable<UserBindTourDTO> {
    return this.http.delete<UserBindTourDTO>(this.bindUserTourUrl + username + '/' + tour, {withCredentials : true});}

  public getAllPersonsForTour(tour: string): Observable<UserBindTourDTO[]> {
    return this.http.get<UserBindTourDTO[]>(this.bindUserTourUrl + tour, {withCredentials : true});}

  public existPersonBindTour(username: string, tour: string):Observable<Boolean> {
    return this.http.get<Boolean>(this.bindUserTourUrl + 'exist/' + username + '/' + tour, {withCredentials : true});}

  public createOrder(order: OrderDTO): Observable<OrderDTO>{
    return this.http.post<OrderDTO>(this.orderUrl, order,{withCredentials : true});}

  public putOrder(order: OrderDTO): Observable<OrderDTO>{
    return this.http.put<OrderDTO>(this.orderUrl, order, {withCredentials : true});}

  public getOrder(people: UserDTO, product: ProductDTO, productdetails: ProductDetailsDTO, date: DatesDTO, tour: TourDTO): Observable<OrderDTO> {
    return this.http.get<OrderDTO>(this.orderUrl + people.username + '/' + product.name + '/' +
      productdetails.id + '/' + date.id + '/' + tour.number , {withCredentials : true});}

  public existOrder(people: UserDTO, product: ProductDTO, productdetails: ProductDetailsDTO, date: DatesDTO, tour: TourDTO): Observable<Boolean> {
    return this.http.get<Boolean>(this.orderUrl + "exist/" + people.username + '/' + product.name + '/' +
      productdetails.id + '/' + date.id + '/' + tour.number , {withCredentials : true});}

  public deleteOrder(people: UserDTO, product: ProductDTO, productdetails: ProductDetailsDTO, date: DatesDTO, tour: TourDTO): Observable<OrderDTO> {
    return this.http.delete<OrderDTO>(this.orderUrl + people.username + '/' + product.name + '/' +
      productdetails.id + '/' + date.id + '/' + tour.number , {withCredentials : true});}

  public getAllOrderForPerson(people: UserDTO): Observable<OrderDTO[]> {
    return this.http.get<OrderDTO[]>(this.orderUrl + people.username, {withCredentials : true});}

  public getAllOrderForPersonBetween(startDate: DatesDTO, endDate: DatesDTO, people: UserDTO): Observable<OrderDTO[]> {
    return this.http.get<OrderDTO[]>(this.orderUrl + 'between/' + startDate.id + '/' + endDate.id + '/' +
      people.username, {withCredentials : true});}

  public getAllOrderForTourAndDate(tour: TourDTO, dates: DatesDTO): Observable<OrderDTO[]> {
    return this.http.get<OrderDTO[]>(this.orderUrl + tour.number + '/' + dates.id, {withCredentials : true});}

  public createUserProfileOrder(pPO: UserProfileOrderDTO): Observable<UserProfileOrderDTO>{
    return this.http.post<UserProfileOrderDTO>(this.userProfileOrderUrl, pPO, {withCredentials : true});}

  public putUserProfileOrder(pPO: UserProfileOrderDTO): Observable<UserProfileOrderDTO>{
    return this.http.put<UserProfileOrderDTO>(this.userProfileOrderUrl, pPO, {withCredentials : true});}

  public getUserProfileOrder(people: UserDTO, product: ProductDTO, productdetails: ProductDetailsDTO, tour: TourDTO): Observable<UserProfileOrderDTO> {
    return this.http.get<UserProfileOrderDTO>(this.userProfileOrderUrl + people.username + '/' + product.name + '/' +
      productdetails.id + '/' + tour.number, {withCredentials : true});}

  public existUserProfileOrder(people: UserDTO, product: ProductDTO, productdetails: ProductDetailsDTO, tour: TourDTO): Observable<Boolean> {
    return this.http.get<Boolean>(this.userProfileOrderUrl + people.username + '/' + product.name + '/' +
      productdetails.id + '/' + tour.number, {withCredentials : true});}

  public deleteUserProfileOrder(people: UserDTO, product: ProductDTO, productdetails: ProductDetailsDTO, tour: TourDTO): Observable<UserProfileOrderDTO> {
    return this.http.delete<UserProfileOrderDTO>(this.userProfileOrderUrl + people.username + '/' + product.name + '/' +
      productdetails.id + '/' + tour.number, {withCredentials : true});}

  public getAllUserProfileOrderForPerson(people: UserDTO): Observable<UserProfileOrderDTO[]> {
    return this.http.get<UserProfileOrderDTO[]>(this.userProfileOrderUrl + people.username, {withCredentials : true});}

  public getAllUserProfileOrder(): Observable<UserProfileOrderDTO[]> {
    return this.http.get<UserProfileOrderDTO[]>(this.userProfileOrderUrl, {withCredentials : true});}

  public createInvoice(invoice: InvoiceDTO): Observable<InvoiceDTO>{
    return this.http.post<InvoiceDTO>(this.invoiceUrl, invoice,{withCredentials : true});}

  public putInvoice(invoice: InvoiceDTO): Observable<InvoiceDTO>{
    return this.http.put<InvoiceDTO>(this.invoiceUrl, invoice,{withCredentials : true});}

  public getInvoice(id: number): Observable<InvoiceDTO>{
    return this.http.get<InvoiceDTO>(this.invoiceUrl + id,{withCredentials : true});}

  public deleteInvoice(id: number): Observable<InvoiceDTO>{
    return this.http.delete<InvoiceDTO>(this.invoiceUrl + id,{withCredentials : true});}

  public existInvoice(id: number): Observable<InvoiceDTO>{
    return this.http.get<InvoiceDTO>(this.invoiceUrl + 'exist/' + id,{withCredentials : true});}

  public createUserBindInvoice(userBindInvoice: UserBindInvoiceDTO): Observable<UserBindInvoiceDTO>{
    return this.http.post<UserBindInvoiceDTO>(this.userBindInvoiceUrl, userBindInvoice,{withCredentials : true});}

  public putUserBindInvoice(userBindInvoice: UserBindInvoiceDTO): Observable<UserBindInvoiceDTO>{
    return this.http.put<UserBindInvoiceDTO>(this.userBindInvoiceUrl, userBindInvoice,{withCredentials : true});}

  public getUserBindInvoice(dateFrom: DatesDTO, dateTo: DatesDTO, invoice: UserDTO, deliver: UserDTO): Observable<UserBindInvoiceDTO>{
    return this.http.get<UserBindInvoiceDTO>(this.userBindInvoiceUrl + dateFrom.id + '/' + dateTo.id + '/' +
      invoice.username + '/' + deliver.username,{withCredentials : true});}

  public deleteUserBindInvoice(dateFrom: DatesDTO, dateTo: DatesDTO, invoice: UserDTO, deliver: UserDTO): Observable<UserBindInvoiceDTO>{
    return this.http.delete<UserBindInvoiceDTO>(this.userBindInvoiceUrl + dateFrom.id + '/' + dateTo.id + '/' +
      invoice.username + '/' + deliver.username,{withCredentials : true});}

  public existUserBindInvoice(dateFrom: DatesDTO, dateTo: DatesDTO, invoice: UserDTO, deliver: UserDTO): Observable<UserBindInvoiceDTO>{
    return this.http.get<UserBindInvoiceDTO>(this.userBindInvoiceUrl + 'exist/' + dateFrom.id + '/' + dateTo.id + '/' +
      invoice.username + '/' + deliver.username,{withCredentials : true});}

  public getAllPersonBindInvoiceForDeliver(deliver: UserDTO): Observable<UserBindInvoiceDTO[]>{
    return this.http.get<UserBindInvoiceDTO[]>(this.userBindInvoiceUrl + 'deliver/' + deliver.username,{withCredentials : true});}

  public getAllPersonBindInvoiceForInvoice(invoice: UserDTO): Observable<UserBindInvoiceDTO[]>{
    return this.http.get<UserBindInvoiceDTO[]>(this.userBindInvoiceUrl + 'invoice/' + invoice.username,{withCredentials : true});}

  public getAllPersonBindInvoice(): Observable<UserBindInvoiceDTO[]>{
    return this.http.get<UserBindInvoiceDTO[]>(this.userBindInvoiceUrl,{withCredentials : true});}

  public getAllPersonBindInvoiceDateFrom(date: DatesDTO): Observable<UserBindInvoiceDTO[]>{
    return this.http.get<UserBindInvoiceDTO[]>(this.userBindInvoiceUrl + date.date,{withCredentials : true});}

  public createUserBindPhone(userBindPhone: UserBindPhoneDTO): Observable<UserBindPhoneDTO>{
    return this.http.post<UserBindPhoneDTO>(this.userBindPhoneUrl, userBindPhone,{withCredentials : true});}

  public putUserBindPhone(userBindPhone: UserBindPhoneDTO): Observable<UserBindPhoneDTO>{
    return this.http.put<UserBindPhoneDTO>(this.userBindPhoneUrl, userBindPhone,{withCredentials : true});}

  public getUserBindPhone(user: UserDTO): Observable<UserBindPhoneDTO>{
    return this.http.get<UserBindPhoneDTO>(this.userBindPhoneUrl + user.username,{withCredentials : true});}

  public deleteUserBindPhone(user: UserDTO): Observable<UserBindPhoneDTO>{
    return this.http.delete<UserBindPhoneDTO>(this.userBindPhoneUrl + user.username,{withCredentials : true});}

  public existUserBindPhone(user: UserDTO): Observable<UserBindPhoneDTO>{
    return this.http.get<UserBindPhoneDTO>(this.userBindPhoneUrl + 'exist/' + user.username,{withCredentials : true});}

  public createAddress(address: AddressDTO): Observable<AddressDTO>{
    return this.http.post<AddressDTO>(this.addressUrl, address,{withCredentials : true});}

  public putAddress(address: AddressDTO): Observable<AddressDTO>{
    return this.http.put<AddressDTO>(this.addressUrl, address,{withCredentials : true});}

  public getAddress(address: AddressDTO): Observable<AddressDTO>{
    return this.http.get<AddressDTO>(this.addressUrl + '/' + address.id , {withCredentials : true});}

  public deleteAddress(address: AddressDTO): Observable<AddressDTO>{
    return this.http.delete<AddressDTO>(this.addressUrl + '/' + address.id , {withCredentials : true});}

  public createUserBindAddress(userBindAddress: UserBindDeliverAddressDTO): Observable<UserBindDeliverAddressDTO>{
    return this.http.post<UserBindDeliverAddressDTO>(this.userBindAddress, userBindAddress,{withCredentials : true});}

  public putUserBindAddress(userBindAddress: UserBindDeliverAddressDTO): Observable<UserBindDeliverAddressDTO>{
    return this.http.put<UserBindDeliverAddressDTO>(this.userBindAddress, userBindAddress,{withCredentials : true});}

  public getUserBindAddress(user: UserDTO): Observable<UserBindDeliverAddressDTO>{
    return this.http.get<UserBindDeliverAddressDTO>(this.userBindAddress + '/' + user.username, {withCredentials : true});}

  public existUserBindAddress(user: UserDTO): Observable<Boolean>{
    return this.http.get<Boolean>(this.userBindAddress + '/exist/' + user.username, {withCredentials : true});}

  public deleteUserBindAddress(user: UserDTO): Observable<UserBindDeliverAddressDTO>{
    return this.http.delete<UserBindDeliverAddressDTO>(this.userBindAddress + '/' + user.username, {withCredentials : true});}

  public getReference(name: String): Observable<ReferenceDTO>{
    return this.http.get<ReferenceDTO>(this.invoiceUrl + 'reference/' + name, {withCredentials : true});}

  public postReference(reference: ReferenceDTO): Observable<ReferenceDTO>{
    return this.http.post<ReferenceDTO>(this.invoiceUrl + 'reference', reference, {withCredentials : true});}

  public putReference(reference: ReferenceDTO): Observable<ReferenceDTO>{
    return this.http.put<ReferenceDTO>(this.invoiceUrl + 'reference', reference, {withCredentials : true});}

}

