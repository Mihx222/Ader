import {Component, OnInit} from '@angular/core';
import {CategoryService} from "../../service/category/category.service";
import {CategoryViewModel} from "../../model/category/category-view-model";
import {FormControl} from "@angular/forms";
import {Observable} from "rxjs";
import {FileService} from "../../service/file/file.service";
import {Offer} from "../../model/offer/offer";
import {OfferService} from "../../service/offer/offer.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-new-offer',
  templateUrl: './new-offer-component.html',
  styleUrls: ['./new-offer.component.css']
})
export class NewOfferComponent implements OnInit {
  uploadedFiles: any[] = [];
  selectedCategories = new FormControl();
  offerName = new FormControl();
  offerExpiryDate = new FormControl();
  offerDescription = new FormControl();
  categories: Observable<CategoryViewModel[]>;

  newOffer: Offer;

  constructor(
      categoryService: CategoryService,
      public fileService: FileService,
      public offerService: OfferService,
      public router: Router
  ) {
    this.categories = categoryService.getCategories();

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
      name: this.offerName.value,
      assigneeName: null,
      bids: [],
      categories: this.selectedCategories.value.map(category => {
        return {name: category}
      }),
      description: this.offerDescription.value,
      expireDate: this.offerExpiryDate.value,
      files: images
    }

    this.offerService.createOffer(this.newOffer).subscribe(
        result => {
          this.router.navigate(['offers']);
        },
        error => {
          console.log(error);
        }
    );
  }

  cancel() {
    this.router.navigate(['/']);
  }
}
