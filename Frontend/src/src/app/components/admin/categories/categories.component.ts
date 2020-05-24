import {Component, OnInit, ViewChild} from '@angular/core';
import {MatTableDataSource} from "@angular/material/table";
import {CategoryViewModel} from "../../../model/category/category-view-model";
import {CategoryService} from "../../../service/category/category.service";
import {ActivatedRoute} from "@angular/router";
import {MatSort} from "@angular/material/sort";
import {MatPaginator} from "@angular/material/paginator";
import {Category} from "../../../model/category/category";

@Component({
  selector: 'app-categories',
  templateUrl: './categories.component.html',
  styleUrls: ['./categories.component.css']
})
export class CategoriesComponent implements OnInit {
  dataSource: MatTableDataSource<CategoryViewModel>;
  categories: CategoryViewModel[] = [];
  columnsToDisplay = ['name', 'actions'];

  @ViewChild(MatSort, {static: true}) sort: MatSort;
  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;
  @ViewChild('input', {static: true}) categoryInput;

  constructor(
      public categoryService: CategoryService,
      private activatedRoute: ActivatedRoute
  ) {
    this.categories = this.activatedRoute.snapshot.data['categories'];
  }

  ngOnInit() {
    this.dataSource = new MatTableDataSource(this.categories);
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  deleteCategory(categoryName: string) {
    this.categoryService.deleteCategory(categoryName).subscribe(
        result => {
          this.categories.splice(
              this.categories.findIndex(category => category.name === categoryName),
              1
          );
          this.dataSource._updateChangeSubscription();
        },
        error => {
          console.log(error);
        }
    );
  }

  addCategory(categoryName: string) {
    let newCategory: Category = {
      name: categoryName
    }
    this.categoryService.addCategory(newCategory).subscribe(
        result => {
          this.categories.push(result);
          this.dataSource._updateChangeSubscription();
          this.categoryInput.nativeElement.value = '';
        },
        error => {
          console.log(error);
        }
    );
  }
}
