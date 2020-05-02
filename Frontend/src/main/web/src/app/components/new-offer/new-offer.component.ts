import {Component, OnInit} from '@angular/core';
import {CategoryService} from "../../service/category/category.service";
import {CategoryViewModel} from "../../model/category/category-view-model";
import {FormControl} from "@angular/forms";

@Component({
  selector: 'app-new-offer',
  templateUrl: './new-offer-component.html',
  styleUrls: ['./new-offer.component.css']
})
export class NewOfferComponent implements OnInit {
  uploadedImages: any[] = [];
  selectedCategories = new FormControl();
  categories: CategoryViewModel[] = [];

  constructor(categoryService: CategoryService) {
    categoryService.getCategories().subscribe(
      result => {
        this.categories = result;
      },
      error => {
        alert(error.message);
      }
    );
  }

  ngOnInit() {
  }

  onFileUploadComplete(data: any) {
    this.uploadedImages.push({
      name: data.name,
      url: 'data:' + data.type + ';base64,' + data.picByte
    });
  }
}
