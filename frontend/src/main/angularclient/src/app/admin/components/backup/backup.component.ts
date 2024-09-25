import { Component } from '@angular/core';
import {TourServiceService} from "../tour/service/tour-service.service";
import {UserService} from "../../../components/user/service/user-service.service";
import {EmailService} from "../email/email-service/email.service";
import {ProductService} from "../product/service/product.service";
import {OrderDTO} from "../../../components/user/service/orderDTO";
import {firstValueFrom} from "rxjs";

@Component({
  selector: 'app-backup',
  templateUrl: './backup.component.html',
  styleUrls: ['./backup.component.css']
})
export class BackupComponent {
  error: string ="";
  success: string ="";

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
}
