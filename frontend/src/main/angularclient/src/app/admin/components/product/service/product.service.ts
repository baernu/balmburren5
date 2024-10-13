import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {ProductDTO} from "./ProductDTO";
import {ProductDetailsDTO} from "./ProductDetailsDTO";
import {ProductBindInfosDTO} from "./ProductBindInfosDTO";
import {CardDTO} from "./cardDTO";


@Injectable({
  providedIn: 'root'
})
export class ProductService {
  // private readonly baseUrl: string = 'https://service.balmburren.net:8006/api/';
  private readonly baseUrl: string = 'http://localhost:8006/api/';
  // private readonly baseUrl: string = 'api/';
  private readonly productUrl: string;
  private readonly productDetailsUrl: string;
  private readonly productBindDetailsUrl: string;
  private readonly resetUrl: string;
  private readonly cardUrl: string;

  constructor(private http: HttpClient) {
    this.productUrl = this.baseUrl + 'pr/product/';
    this.productDetailsUrl = this.baseUrl + 'pr/product/details/';
    this.productBindDetailsUrl = this.baseUrl + 'pr/product/bind/infos/';
    this.resetUrl = this.baseUrl + 're/reset/';
    this.cardUrl = this.baseUrl + 'cd/card/';


  }

  public createProduct(product: ProductDTO):Observable<ProductDTO> {
    return this.http.post<ProductDTO>(this.productUrl, product,{withCredentials: true});}

  public putProduct(product: ProductDTO):Observable<ProductDTO> {
    return this.http.put<ProductDTO>(this.productUrl, product,{withCredentials: true});}

  public getProduct(name: string):Observable<ProductDTO> {
    return this.http.get<ProductDTO>(this.productUrl + name, {withCredentials: true});}

  public deleteProduct(product: ProductDTO):Observable<ProductDTO> {
    return this.http.patch<ProductDTO>(this.productUrl, product,{withCredentials: true});}

  public getProducts():Observable<ProductDTO[]>{
    return this.http.get<ProductDTO[]>(this.productUrl, {withCredentials: true});}

  public existProduct(name: string):Observable<Boolean> {
    return this.http.get<Boolean>(this.productUrl + "exist/" + name,{withCredentials: true});}

  public createProductDetails(productDetails: ProductDetailsDTO):Observable<ProductDetailsDTO> {
    return this.http.post<ProductDetailsDTO>(this.productDetailsUrl, productDetails,{withCredentials: true});}

  public putProductDetails(productDetails: ProductDetailsDTO):Observable<ProductDetailsDTO> {
    return this.http.put<ProductDetailsDTO>(this.productDetailsUrl, productDetails,{withCredentials: true});}

  public getProductDetails(id: string):Observable<ProductDetailsDTO> {
    return this.http.get<ProductDetailsDTO>(this.productDetailsUrl + id, {withCredentials: true});}

  public deleteProductDetails(productDetails: ProductDetailsDTO):Observable<ProductDetailsDTO> {
    return this.http.patch<ProductDetailsDTO>(this.productDetailsUrl, productDetails,{withCredentials: true});}

  public getAllProductDetailsForCategory(category: string):Observable<ProductDetailsDTO[]> {
    return this.http.get<ProductDetailsDTO[]>(this.productDetailsUrl + category, {withCredentials: true});}

  public getAllProductDetails():Observable<ProductDetailsDTO[]> {
    return this.http.get<ProductDetailsDTO[]>(this.productDetailsUrl, {withCredentials: true});}

  public createProductBindInfos(productBindInfos: ProductBindInfosDTO):Observable<ProductBindInfosDTO> {
    return this.http.post<ProductBindInfosDTO>(this.productBindDetailsUrl, productBindInfos,{withCredentials: true});}

  public putProductBindInfos(productBindInfos: ProductBindInfosDTO):Observable<ProductBindInfosDTO> {
    return this.http.put<ProductBindInfosDTO>(this.productBindDetailsUrl, productBindInfos,{withCredentials: true});}

  public getProductBindInfosisChecked(bool: boolean):Observable<ProductBindInfosDTO[]> {
    return this.http.get<ProductBindInfosDTO[]>(this.productBindDetailsUrl + 'ischecked/' + bool , {withCredentials: true});}

  public getProductBindInfos(product: ProductDTO, productDetails: ProductDetailsDTO):Observable<ProductBindInfosDTO> {
    return this.http.get<ProductBindInfosDTO>(this.productBindDetailsUrl + product.name + '/' + productDetails.id , {withCredentials: true});}

  public isProductBindInfos(product: ProductDTO, productDetails: ProductDetailsDTO):Observable<Boolean> {
    return this.http.get<Boolean>(this.productBindDetailsUrl + 'exist/' +  product.name + '/' + productDetails.id , {withCredentials: true});}

  public getProductBindInfosById(id: number):Observable<ProductBindInfosDTO> {
    return this.http.get<ProductBindInfosDTO>(this.productBindDetailsUrl + 'byid/' +id , {withCredentials: true});}

  public deleteProductBindInfos(productBindInfos: ProductBindInfosDTO):Observable<ProductBindInfosDTO> {
    return this.http.patch<ProductBindInfosDTO>(this.productBindDetailsUrl ,productBindInfos, {withCredentials: true});}

  public getAllProductBindInfosForProduct(product: ProductDTO):Observable<ProductBindInfosDTO[]> {
    return this.http.get<ProductBindInfosDTO[]>(this.productBindDetailsUrl + product.name , {withCredentials: true});}

  public getAllProductBindInfos():Observable<ProductBindInfosDTO[]> {
    return this.http.get<ProductBindInfosDTO[]>(this.productBindDetailsUrl, {withCredentials: true});}

  public resetWithFlyway():Observable<string> {
    return this.http.get<string>(this.resetUrl, {withCredentials: true});}

  public saveCard(cardDTO: CardDTO):Observable<CardDTO> {
    return this.http.post<CardDTO>(this.cardUrl, cardDTO,{withCredentials: true});}

  public deleteCard(cardDTO: CardDTO):Observable<CardDTO> {
    return this.http.patch<CardDTO>(this.cardUrl, cardDTO,{withCredentials: true});}

  public findCard(header: string, subheader: string):Observable<CardDTO> {
    return this.http.get<CardDTO>(this.cardUrl + header + '/' + subheader,{withCredentials: true});}

  public findCardById(id: string):Observable<CardDTO> {
    return this.http.get<CardDTO>(this.cardUrl + id,{withCredentials: true});}

  public findAllActiveCards(isactive: boolean):Observable<CardDTO[]> {
    return this.http.get<CardDTO[]>(this.cardUrl  + 'isactive/' + isactive,{withCredentials: true});}

  public findAllCards():Observable<CardDTO[]> {
    return this.http.get<CardDTO[]>(this.cardUrl ,{withCredentials: true});}

  public existCard(header: string, subheader: string):Observable<Boolean> {
    return this.http.get<Boolean>(this.cardUrl + 'exist/' + header + '/' + subheader,{withCredentials: true});}
}
