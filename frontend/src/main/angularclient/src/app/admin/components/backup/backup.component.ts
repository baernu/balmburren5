import { Component } from '@angular/core';
import {EmailService} from "../email/email-service/email.service";
import {firstValueFrom} from "rxjs";
import {EmailDataDTO} from "../email/email-service/EmailDataDTO";



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
  file: File | undefined;
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

  async uploadFile(event: Event) {
    this.success1 ="";
    this.error1 = "";
    const element = event.currentTarget as HTMLInputElement;
    let fileList: FileList | null = element.files;
    if (fileList) {
      this.file = fileList[0];
      this.filename = fileList[0].name;
      const byteArray = await this.fileToByteArray(this.file);
      this.emaildata.type = "attachment";
      this.emaildata.name = this.filename;

    }
  }

  async fileToByteArray(file: File) {
    const arrayBuffer = await file.arrayBuffer();
    return new Uint8Array(arrayBuffer); // Returning the byte array
  }

}
