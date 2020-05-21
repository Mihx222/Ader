import {Component, OnInit} from '@angular/core';
import {CategoryService} from "../../service/category/category.service";
import {CategoryViewModel} from "../../model/category/category-view-model";
import {FormControl, Validators} from "@angular/forms";
import {Observable} from "rxjs";
import {FileService} from "../../service/file/file.service";
import {Offer} from "../../model/offer/offer";
import {OfferService} from "../../service/offer/offer.service";
import {Router} from "@angular/router";
import {CustomErrorStateMatcher} from "../../helpers/custom-error-state-matcher";
import {AdvertisementFormatViewModel} from "../../model/advertisementformat/advertisement-format-view-model";
import {AdvertisementFormatService} from "../../service/advertisementformat/advertisement-format.service";
import {Location} from "@angular/common";

@Component({
  selector: 'app-new-offer',
  templateUrl: './new-offer-component.html',
  styleUrls: ['./new-offer.component.css']
})
export class NewOfferComponent implements OnInit {

  matcher = new CustomErrorStateMatcher();
  uploadedFiles: any[] = [];
  selectedCategories = new FormControl();
  selectedAdvertisementFormats = new FormControl();
  offerName = new FormControl('', [Validators.required]);
  compensation = new FormControl('', [Validators.required]);
  offerExpiryDate = new FormControl('', [Validators.required]);
  offerDescription = new FormControl('', [Validators.required]);
  categories: Observable<CategoryViewModel[]>;
  advertisementFormats: Observable<AdvertisementFormatViewModel[]>;
  freeProductSample: boolean = false;
  advertisementReview: boolean = true;

  newOffer: Offer;

  constructor(
      categoryService: CategoryService,
      advertisementFormatService: AdvertisementFormatService,
      public fileService: FileService,
      public offerService: OfferService,
      public router: Router,
      public location: Location
  ) {
    this.categories = categoryService.getCategories();
    this.advertisementFormats = advertisementFormatService.getAdvertisementFormats();

    this.fileService.getFilesForUser(true).subscribe(
        result => {
          this.uploadedFiles.splice(0, this.uploadedFiles.length);
          this.convertResults(result);
        },
        error => {
          console.log(error);
        }
    );
  }

  ngOnInit() {
  }

  convertResult(data: any) {
    this.uploadedFiles.push({
      original: data,
      name: data.name,
      url: 'data:' + data.type + ';base64,' + data.bytes
    });
  }

  convertResults(data: any[]) {
    data.forEach(item => {
      this.uploadedFiles.push({
        original: item,
        name: item.name,
        url: 'data:' + item.type + ';base64,' + item.bytes
      });
    })
  }

  onFileUploadComplete(data: any) {
    if (data.type.includes('image')) {
      this.convertResult(data);
    } else {
      console.log("It's not an image");
    }
  }

  removeFile(image: any) {
    this.fileService.deleteFile(image.original.uuid).subscribe(
        result => {
          const index = this.uploadedFiles.indexOf(image);
          if (index > -1) {
            this.uploadedFiles.splice(index, 1);
          }
        },
        error => {
          console.log(error);
        }
    );
  }

  createOffer() {
    const images: any[] = [];

    this.uploadedFiles.forEach(file => {
      images.push(file.original);
    })

    this.newOffer = {
      id: null,
      name: this.offerName.value,
      assigneeName: null,
      bids: [],
      categories: this.selectedCategories.value.map(category => {
        return {name: category}
      }),
      advertisementFormats: this.selectedAdvertisementFormats.value.map(advertisementFormat => {
        return {name: advertisementFormat}
      }),
      freeProductSample: this.freeProductSample,
      advertisementReview: this.advertisementReview,
      compensation: this.compensation.value,
      description: this.offerDescription.value,
      expireDate: this.offerExpiryDate.value,
      files: images
    }

    this.offerService.createOffer(this.newOffer).subscribe(
        result => {
          this.router.navigate(['offers/' + result.id]);
        },
        error => {
          console.log(error);
        }
    );
  }

  checkForErrors() {
    return this.offerName.hasError('required') ||
        this.offerExpiryDate.hasError('required') ||
        this.offerDescription.hasError('required') ||
        this.compensation.hasError('required');
  }

  cancel() {
    this.location.back();
  }
}
