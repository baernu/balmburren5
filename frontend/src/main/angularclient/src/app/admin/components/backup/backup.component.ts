import { Component } from '@angular/core';
import {EmailService} from "../email/email-service/email.service";
import {firstValueFrom} from "rxjs";
import {OrderDTO} from "../../../components/user/service/orderDTO";
import {ByteDTO} from "../../../components/user/service/byteDTO";




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
  byteDTO: ByteDTO = new ByteDTO();

  constructor(
    private emailService: EmailService,) {
  }

  async backupToFile() {
    try {
      await firstValueFrom(this.emailService.backupWriteToFile());

    } catch (error: any) {
      if (error.status !== 200) {
        this.error = "Backup konnte nicht geladen werden!";
        setTimeout(() => {
          this.error = "";
        }, 2000);
        return;
      }
    }
    this.success = "Backup wurde geladen.";
    setTimeout(() => {
      this.success = "";
    }, 1000);
  }

  async backupSend() {
    try {
      await firstValueFrom(this.emailService.backupSend());

    } catch (error: any) {
      if (error.status !== 200) {
        this.error = "Backup konnte nicht gesendet werden!";
        setTimeout(() => {
          this.error = "";
        }, 2000);
        return;
      }
    }
    this.success = "Backup wurde gesendet.";
    setTimeout(() => {
      this.success = "";
    }, 1000);
  }

  async importBackup(event: Event) {
    let file: File;
    let filename: string;
    const element = event.currentTarget as HTMLInputElement;
    let fileList: FileList | null = element.files;
    if (fileList) {
      let orders: OrderDTO[] = [];
      file = fileList[0];
      filename = fileList[0].name;

      try {
        let data= await this.fileToByteArray(file);
        this.byteDTO.bytearray = this.byteToBase64(data);
        console.log("Bytearray: " + this.byteDTO.bytearray);
        await firstValueFrom(this.emailService.backupImport(this.byteDTO));

      } catch (error: any) {
        if (error.status !== 200) {
          this.error1 = "Backup Import konnte nicht geladen werden!!";
          setTimeout(() => {
            this.error1 = "";
          }, 2000);
          return;
        }
      }
      this.success1 = "Backup Import wurde geladen.";
      setTimeout(() => {
        this.success1 = "";
      }, 1000);
    }
  }

  async fileToByteArray(file: File) {
    const arrayBuffer = await file.arrayBuffer();
    return new Uint8Array(arrayBuffer); // Returning the byte array
  }

  byteToBase64(byteArray: Uint8Array) {
    // Convert Uint8Array to base64 string
    const base64String = btoa(String.fromCharCode(...byteArray));
    return base64String;

  }
}
