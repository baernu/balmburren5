import { Component } from '@angular/core';
import {EmailService} from "../email/email-service/email.service";
import {firstValueFrom} from "rxjs";



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
      // var new_zip = new JSZip();
      // new_zip.loadAsync(this.file);
      // new_zip.files["doc.xml"].asText() // this give you the text in the file

    }
  }
}
