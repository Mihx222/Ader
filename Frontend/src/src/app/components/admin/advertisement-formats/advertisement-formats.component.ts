import {Component, OnInit, ViewChild} from '@angular/core';
import {MatTableDataSource} from "@angular/material/table";
import {MatSort} from "@angular/material/sort";
import {MatPaginator} from "@angular/material/paginator";
import {ActivatedRoute} from "@angular/router";
import {Category} from "../../../model/category/category";
import {AdvertisementFormatViewModel} from "../../../model/advertisementformat/advertisement-format-view-model";
import {AdvertisementFormatService} from "../../../service/advertisementformat/advertisement-format.service";

@Component({
  selector: 'app-advertisement-formats',
  templateUrl: './advertisement-formats.component.html',
  styleUrls: ['./advertisement-formats.component.css']
})
export class AdvertisementFormatsComponent implements OnInit {
  dataSource: MatTableDataSource<AdvertisementFormatViewModel>;
  formats: AdvertisementFormatViewModel[] = [];
  columnsToDisplay = ['name', 'actions'];

  @ViewChild(MatSort, {static: true}) sort: MatSort;
  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;
  @ViewChild('input', {static: true}) advertisementFormatInput;

  constructor(
      public advertisementFormatService: AdvertisementFormatService,
      private activatedRoute: ActivatedRoute
  ) {
    this.formats = this.activatedRoute.snapshot.data['formats'];
  }

  ngOnInit() {
    this.dataSource = new MatTableDataSource(this.formats);
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  deleteAdvertisementFormat(advertisementFormatName: string) {
    this.advertisementFormatService.deleteAdvertisementFormat(advertisementFormatName).subscribe(
        result => {
          this.formats.splice(
              this.formats.findIndex(format => format.name === advertisementFormatName),
              1
          );
          this.dataSource._updateChangeSubscription();
        },
        error => {
          console.log(error);
        }
    );
  }

  AddAdvertisementFormat(advertisementFormatName: string) {
    let newAdvertisementFormat: Category = {
      name: advertisementFormatName
    }
    this.advertisementFormatService.addAdvertisementFormat(newAdvertisementFormat).subscribe(
        result => {
          this.formats.push(result);
          this.dataSource._updateChangeSubscription();
          this.advertisementFormatInput.nativeElement.value = '';
        },
        error => {
          console.log(error);
        }
    );
  }
}
