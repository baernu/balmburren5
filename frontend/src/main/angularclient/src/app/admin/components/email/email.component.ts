import {Component, OnInit} from '@angular/core';
import {EmailDataDTO} from "./email-service/EmailDataDTO";
import {UserService} from "../../../components/user/service/user-service.service";
import {EmailService} from "./email-service/email.service";
import {UserDTO} from "../../../components/user/service/userDTO";
import {firstValueFrom} from "rxjs";
import {UserBindPhoneDTO} from "../../../components/user/service/UserBindPhoneDTO";
import {EmailUserDTO} from "./email-service/emailUserDTO";

@Component({
  selector: 'app-email',
  templateUrl: './email.component.html',
  styleUrls: ['./email.component.css']
})
export class EmailComponent implements OnInit {
  emailData: EmailDataDTO = new EmailDataDTO();
  users: UserDTO[] = [];
  user: UserDTO = new UserDTO();
  showPassword: boolean = false;
  all: Boolean = false;
  emailUsers: EmailUserDTO[] = [];

  constructor(
    private emailService: EmailService,
    private userService: UserService) {
  }

  async ngOnInit(): Promise<void> {
    let users = await firstValueFrom(this.userService.findAll());
    for (const user of users) {
      let bool = await firstValueFrom(this.userService.existUserBindPhone(user));
      let isUser = await firstValueFrom(this.userService.isBasic(user.username));
      if (bool && isUser ) {
        this.users.push(user);
        let personbindphone: UserBindPhoneDTO = await firstValueFrom(this.userService.getUserBindPhone(user));
        let emailUser: EmailUserDTO = new EmailUserDTO();
        emailUser.email = personbindphone.email;
        emailUser.user = user;
        emailUser.phone = personbindphone.phone;
        this.emailUsers.push(emailUser);
      }
    }
  }


  async email() {
    for (const emailUser of this.emailUsers) {
      this.emailData.toEmail = emailUser.email;
      if (!this.emailData.file && emailUser.email) {
        this.emailData.type = "normal";
        await firstValueFrom(this.emailService.sendEmail(this.emailData));
        console.log("Enail to first: " + this.emailData.toEmail);
      }
      if (this.emailData.file && emailUser.email) {
        this.emailData.type = "attachment";
        await firstValueFrom(this.emailService.sendEmail(this.emailData));
        console.log("Enail to second: " + this.emailData.toEmail)
      }
    }
  }


  handleChunk(buf: Uint8Array) {
    let fileByteArray = [];
    for (let i = 0; i < buf.length; i++) {
      fileByteArray.push(buf[i]);
    }
    this.emailData.byteArray = fileByteArray;
  }

  async uploadFile(event: Event) {
    const element = event.currentTarget as HTMLInputElement;
    let fileList: FileList | null = element.files;
    if (fileList)
      this.emailData.filename = fileList[0].name;
    if (fileList && this.emailData.file && fileList[0].name.endsWith(".txt")) {
      let data: any = await this.readFile(fileList[0]);
      this.handleChunk(new Uint8Array(data));
    }
    if (fileList && this.emailData.file && fileList[0].name.endsWith(".pdf")) {
      this.emailData.base64String = <string>await this.returnBase64String(fileList[0]);
    }
  }

  async readFile(file: File) {
    return new Promise((resolve, reject) => {
      let reader = new FileReader();
      reader.onload = () => {
        resolve(reader.result);
      };
      reader.onerror = reject;
      reader.readAsArrayBuffer(file);
    })
  }

  async returnBase64String(file: File) {
    return new Promise((resolve, reject) => {
      let reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = () => {
        resolve(reader.result);
      };
      reader.onerror = reject;
    })
  }

  check() {
    if (this.all) {
      for (const emailUser of this.emailUsers) {
        emailUser.isChecked = true;
      }
    } else {
      for (const emailUser of this.emailUsers) {
        emailUser.isChecked = false;
      }
    }
  }

}
