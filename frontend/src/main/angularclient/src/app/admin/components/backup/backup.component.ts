import { Component } from '@angular/core';
import {EmailService} from "../email/email-service/email.service";
import {firstValueFrom} from "rxjs";
import {EmailDataDTO} from "../email/email-service/EmailDataDTO";
import {OrderDTO} from "../../../components/user/service/orderDTO";



@Component({
  selector: 'app-backup',
  templateUrl: './backup.component.html',
  styleUrls: ['./backup.component.css']
})
export class BackupComponent {
  error: string ="";
  success: string ="";
  error1: string ="";
  success1: string ="";
  filename: string = "";
  emaildata: EmailDataDTO | any = new EmailDataDTO();

  constructor(
    private emailService: EmailService,
    ) {
  }

  async backupToFile() {
    this.success ="";
    this.error = "";
    try {
      await firstValueFrom(this.emailService.backupWriteToFile());

    } catch (error: any) {
      if (error.status !== 200)
        this.error = "Backup konnte nicht geladen werden!";
      setTimeout(() => {
        this.error = "";
        return;
      }, 2000);
      return;
    }
    this.success = "Backup wurde geladen.";
    setTimeout(() => {
      this.success = "";
      return;
    }, 1000);
  }

  async backupSend() {
    this.success ="";
    this.error = "";
    try {
      await firstValueFrom(this.emailService.backupSend());

    } catch (error: any) {
      if (error.status !== 200)
        this.error = "Backup konnte nicht gesendet werden!";
      setTimeout(() => {
        this.error = "";
        return;
      }, 2000);
      return;
    }
    this.success = "Backup wurde gesendet.";
    setTimeout(() => {
      this.success = "";
      return;
    }, 1000);
  }

  async importBackup(event: Event) {
    this.success1 ="";
    this.error1 = "";
    let file: File;
    let filename: string;
    const element = event.currentTarget as HTMLInputElement;
    let fileList: FileList | null = element.files;
    if (fileList) {
      let orders: OrderDTO[] = [];
      file = fileList[0];
      filename = fileList[0].name;

      try {
        this.emaildata.byteArray = await this.fileToByteArray(file);
        await firstValueFrom(this.emailService.backupImport(this.emaildata));

      } catch (error: any) {
        if (error.status !== 200)
          this.error1 = "Backup Import konnte nicht geladen werden!!";
        setTimeout(() => {
          this.error1 = "";
          return;
        }, 2000);
        return;
      }
      this.success1 = "Backup Import wurde geladen.";
      setTimeout(() => {
        this.success1 = "";
        return;
      }, 1000);
    }
  }

  async fileToByteArray(file: File) {
    const arrayBuffer = await file.arrayBuffer();
    return new Uint8Array(arrayBuffer); // Returning the byte array
  }


}
