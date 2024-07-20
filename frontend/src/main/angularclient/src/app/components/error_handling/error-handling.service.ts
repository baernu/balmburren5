import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class ErrorHandlingService {
  private readonly loginBoolUrl: string;
  private readonly registerBool1Url: string;
  private readonly registerBool2Url: string;
  // private readonly baseUrl: string = 'http://localhost:8080/api/';
  private readonly baseUrl: string = 'api/';

  constructor(private http: HttpClient) {
    this.loginBoolUrl = this.baseUrl + 'error/handling/bool/login';
    this.registerBool1Url =  this.baseUrl + 'error/handling/bool/register1';
    this.registerBool2Url = this.baseUrl + 'error/handling/bool/register2';

  }

  public getBoolLogin():Observable<Boolean> {
    return this.http.get<Boolean>(this.loginBoolUrl, {withCredentials : true});}

  public putBoolLogin(bool: boolean):Observable<Boolean> {
    return this.http.get<Boolean>(this.loginBoolUrl + '/' + bool, {withCredentials : true});}

  public getBoolRegister1():Observable<Boolean> {
    return this.http.get<Boolean>(this.registerBool1Url, {withCredentials : true});}

  public putBoolRegister1(bool: boolean):Observable<Boolean> {
    return this.http.get<Boolean>(this.registerBool1Url + '/' + bool, {withCredentials : true});}

  public getBoolRegister2():Observable<Boolean> {
    return this.http.get<Boolean>(this.registerBool2Url, {withCredentials : true});}

  public putBoolRegister2(bool: boolean):Observable<Boolean> {
    return this.http.get<Boolean>(this.registerBool2Url + '/' + bool, {withCredentials : true});}
}
