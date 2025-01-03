import {Component, OnInit} from '@angular/core';
import {TourDTO} from "../../service/TourDTO";
import {TourServiceService} from "../../service/tour-service.service";
import {ActivatedRoute, Router} from "@angular/router";
import {firstValueFrom} from "rxjs";
import {TourBindDatesDTO} from "../../service/TourBindDatesDTO";
import {ProductBindInfosDTO} from "../../../product/service/ProductBindInfosDTO";
import {ProductService} from "../../../product/service/product.service";
import {TourDateBindInfosDTO} from "../../service/TourDateBindInfosDTO";
import {DatesDTO} from "../../service/DatesDTO";
import {groupebyDTO} from "../../../../../components/user/service/groupbyDTO";

@Component({
  selector: 'app-tour-data',
  templateUrl: './tour-data.component.html',
  styleUrls: ['./tour-data.component.css']
})
export class TourDataComponent implements OnInit{

  tours: TourDTO[] =[];
  tour: TourDTO = new TourDTO();
  tourBindDatesDTOs: TourBindDatesDTO[] = [];
  dates: Date[] = [];
  param1: string = "";
  productBindInfos: ProductBindInfosDTO[] = [];
  productBindInfosForDate: ProductBindInfosDTO[] = [];
  tourDatesBindInfos: TourDateBindInfosDTO[] = [];
  error: string = "";
  success: string = "";
  categories: groupebyDTO[] = [];


  constructor(
    private tourService: TourServiceService,
    private productService: ProductService,
    private route: ActivatedRoute,
    private router: Router,
  ){
    this.router.routeReuseStrategy.shouldReuseRoute = () => {
      return false;};
  }


  async ngOnInit() {
    let param= this.route.snapshot.queryParamMap.get('param1');
    if (param) {
        this.param1 = param;
        this.tour = await firstValueFrom(this.tourService.getTour(param));
    }
    this.tours = await firstValueFrom(this.tourService.getTours());
    this.productBindInfos = await firstValueFrom(this.productService.getAllProductBindInfos());
    this.productBindInfos = this.checkIfProductBindInfosActive(this.productBindInfos);
    this.productBindInfos.sort((p1:ProductBindInfosDTO, p2:ProductBindInfosDTO) => p1.product.name.localeCompare(p2.product.name));
    this.productBindInfos.sort((p1:ProductBindInfosDTO, p2:ProductBindInfosDTO) => p2.productDetails.price - p1.productDetails.price);
    this.tourDatesBindInfos = await firstValueFrom(this.tourService.getAllTourDatesBindInfos());
    this.tourDatesBindInfos.sort((t1:TourDateBindInfosDTO, t2: TourDateBindInfosDTO) => t1.productBindInfos.productDetails.category.localeCompare(t2.productBindInfos.productDetails.category));
    this.tourDatesBindInfos.sort((t1:TourDateBindInfosDTO, t2: TourDateBindInfosDTO) => t1.dates.date.localeCompare(t2.dates.date));
    this.tourDatesBindInfos = this.tourDatesBindInfos.filter(e => this.compare(new Date(e.dates.date)));
    if(this.param1 != "")this.goTo(this.tour);
  }

  checkIfProductBindInfosActive(productBindInfos: ProductBindInfosDTO[]) {
    return productBindInfos.filter(productBindInfo =>  productBindInfo.endDate.date >= new Date().toISOString().split('T')[0]);
  }

  async goTo(tour: TourDTO) {
    this.tour = tour;
    this.tour = await firstValueFrom(this.tourService.getTour(tour.number));
    this.param1 = tour.number;
    await this.updateDateArray();
  }

  addDates(tour: TourDTO) {
    this.router.navigate(['admin_tour_data_add/'],
      {
        queryParams: {
          param1: this.param1
        }
      });
  }

  async delete(date1: Date) {
    if (date1) {
      let dateString: string = date1.toISOString().split('T')[0];
      if (dateString != null) {
        let tbD = this.tourBindDatesDTOs.find(t => t.dates.date === dateString);
        if (tbD != null)
          await firstValueFrom(this.tourService.deleteTourBindDates(tbD.tour.number, tbD.dates.id));
        await this.updateDateArray();
      }
    }
    await this.ngOnInit();
  }

  async updateDateArray() {
    this.dates = [];
    if (this.param1 != "") {
        this.tourBindDatesDTOs = await firstValueFrom(this.tourService.getAllTourBindDatesForTour(this.param1));
        this.tourBindDatesDTOs.filter(e => this.compare(new Date(e.dates.date)));
    }
    this.tourBindDatesDTOs.forEach(d => {
      if (d.dates.date != "" && this.compare(new Date(d.dates.date))) {
        this.dates.push(new Date(d.dates.date));
      }});
    this.showGroup();
    }

 async apply() {
   this.productBindInfosForDate = [];
    this.productBindInfos.forEach(p => {
      this.updateProductBindInfos(p);
    });
    this.productBindInfosForDate = this.productBindInfos.filter(p => p.isChecked);
  }
  async updateProductBindInfos(p: ProductBindInfosDTO) {
    let p0 = await firstValueFrom(this.productService.getProductBindInfos(p.product, p.productDetails));
    p.version = p0.version;
    let p1 = await firstValueFrom(this.productService.putProductBindInfos(p));
  }

  async del(tourDateBindInfo: TourDateBindInfosDTO) {
    try{
        await firstValueFrom(this.tourService.deleteTourDatesBindInfosById(tourDateBindInfo));
        this.tourDatesBindInfos = this.tourDatesBindInfos.filter(t => t != tourDateBindInfo);
    }catch(error: any){
      if(error.status != 200) {
          this.error = "Löschen hat nicht geklappt!";
          setTimeout(() => {
            this.error = "";
            }, 2000);
          return;
      }
    }
      this.success = "Löschen hat geklappt";
      setTimeout(() => {
        this.success = "";
        this.ngOnInit();
        }, 1000);
  }

 async addInfos(date: Date) {
    let dateString: string = date.toISOString().split('T')[0];
    await this.apply();
    this.productBindInfosForDate.forEach(p => {
      if (p.startDate.date <= dateString && p.endDate.date >= dateString)
        this.addTourDateBindInfo(p, dateString)
    });

   await this.router.navigate(['admin_tour_data/'],
     {
       queryParams: {
         param1: this.param1
       }
     });
  }
  async addTourDateBindInfo(p: ProductBindInfosDTO, dateString: string) {
    let date = new DatesDTO();
    date.date = dateString;
    let tourDateBindInfo = new TourDateBindInfosDTO();
    tourDateBindInfo.tour = this.tour;
    tourDateBindInfo.dates = await firstValueFrom(this.tourService.createDates(date));
    tourDateBindInfo.productBindInfos = p;
    if (!await firstValueFrom(this.tourService.existTourDatesBindInfos(tourDateBindInfo.tour,tourDateBindInfo.dates,
      tourDateBindInfo.productBindInfos.product, tourDateBindInfo.productBindInfos.productDetails)))
        await firstValueFrom(this.tourService.createTourDateBindInfos(tourDateBindInfo));
  }

    compare(date: Date): boolean {
        let now1 = new Date().toISOString().split('T')[0]
        date = new Date(date);
        return date.toISOString().split('T')[0] >= now1;
    }

  showGroup() {
    const group = this.tourDatesBindInfos.reduce((acc: any, curr) => {
      let key = curr.dates.date;
      if (!acc[key]) {
        acc[key] = [];
      }
      acc[key].push(curr);
      return acc;
    }, {});

    //Get the categories and product related.
    this.categories = Object.keys(group).map(key => ({
      category: key,
      products: group[key],
    }));
  }
}
