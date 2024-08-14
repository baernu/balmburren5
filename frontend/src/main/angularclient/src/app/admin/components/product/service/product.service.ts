import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {ProductDTO} from "./ProductDTO";
import {ProductDetailsDTO} from "./ProductDetailsDTO";
import {ProductBindInfosDTO} from "./ProductBindInfosDTO";


@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private readonly baseUrl: string = 'http://localhost:8006/api/';
  // private readonly baseUrl: string = 'api/';
  private readonly productUrl: string;
  private readonly productDetailsUrl: string;
  private readonly productBindDetailsUrl: string;

  constructor(private http: HttpClient) {
    this.productUrl = this.baseUrl + 'pr/product/';
    this.productDetailsUrl = this.baseUrl + 'pr/product/details/';
    this.productBindDetailsUrl = this.baseUrl + '/pr/product/bind/infos/';


  }

  public createProduct(product: ProductDTO):Observable<ProductDTO> {
    return this.http.post<ProductDTO>(this.productUrl, product,{withCredentials: true});}

  public putProduct(product: ProductDTO):Observable<ProductDTO> {
    return this.http.put<ProductDTO>(this.productUrl, product,{withCredentials: true});}

  public getProduct(name: string):Observable<ProductDTO> {
    return this.http.get<ProductDTO>(this.productUrl + name, {withCredentials: true});}

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

  public deleteProductDetails(id: string):Observable<ProductDetailsDTO> {
    return this.http.delete<ProductDetailsDTO>(this.productDetailsUrl + id, {withCredentials: true});}

  public getAllProductDetailsForCategory(category: string):Observable<ProductDetailsDTO[]> {
    return this.http.get<ProductDetailsDTO[]>(this.productDetailsUrl + category, {withCredentials: true});}

  public getAllProductDetails():Observable<ProductDetailsDTO[]> {
    return this.http.get<ProductDetailsDTO[]>(this.productDetailsUrl, {withCredentials: true});}

  public createProductBindInfos(productBindInfos: ProductBindInfosDTO):Observable<ProductBindInfosDTO> {
    return this.http.post<ProductBindInfosDTO>(this.productBindDetailsUrl, productBindInfos,{withCredentials: true});}

  public putProductBindInfos(productBindInfos: ProductBindInfosDTO):Observable<ProductBindInfosDTO> {
    return this.http.put<ProductBindInfosDTO>(this.productBindDetailsUrl, productBindInfos,{withCredentials: true});}

  public getProductBindInfos(product: ProductDTO, productDetails: ProductDetailsDTO):Observable<ProductBindInfosDTO> {
    return this.http.get<ProductBindInfosDTO>(this.productBindDetailsUrl + product.name + '/' + productDetails.id , {withCredentials: true});}

  public isProductBindInfos(product: ProductDTO, productDetails: ProductDetailsDTO):Observable<Boolean> {
    return this.http.get<Boolean>(this.productBindDetailsUrl + 'exist/' +  product.name + '/' + productDetails.id , {withCredentials: true});}

  public getProductBindInfosById(id: number):Observable<ProductBindInfosDTO> {
    return this.http.get<ProductBindInfosDTO>(this.productBindDetailsUrl + 'byid/' +id , {withCredentials: true});}

  public deleteProductBindInfos(product: ProductDTO, productDetails: ProductDetailsDTO):Observable<ProductBindInfosDTO> {
    return this.http.delete<ProductBindInfosDTO>(this.productBindDetailsUrl + product.name + '/' + productDetails.id , {withCredentials: true});}

  public getAllProductBindInfosForProduct(product: ProductDTO):Observable<ProductBindInfosDTO[]> {
    return this.http.get<ProductBindInfosDTO[]>(this.productBindDetailsUrl + product.name , {withCredentials: true});}

  public getAllProductBindInfos():Observable<ProductBindInfosDTO[]> {
    return this.http.get<ProductBindInfosDTO[]>(this.productBindDetailsUrl, {withCredentials: true});}
}
