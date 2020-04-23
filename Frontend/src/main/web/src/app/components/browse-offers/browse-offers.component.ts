import {Component, OnInit, ViewChild} from '@angular/core';
import {OfferService} from "../../service/offer/offer.service";
import {MatTableDataSource} from "@angular/material/table";
import {OfferViewModel} from "../../model/offer/offer-view-model";
import {MatSort} from "@angular/material/sort";
import {MatPaginator} from "@angular/material/paginator";

@Component({
  selector: 'app-browse-offers',
  templateUrl: './browse-offers.component.html',
  styleUrls: ['./browse-offers.component.css']
})
export class BrowseOffersComponent implements OnInit {
  dataSource: MatTableDataSource<OfferViewModel>;
  offers: OfferViewModel[] = [];

  columnsToDisplay = ['name', 'description', 'categories', 'status'];

  @ViewChild(MatSort, {static: true}) sort: MatSort;
  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;

  constructor(public offerService: OfferService) {
  }

  ngOnInit() {
    this.offerService.getOffers().subscribe(
      result => {
        this.dataSource = new MatTableDataSource(result);
        this.offers = result;

        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
      },
      error => {
        alert("Could not retrieve offers!");
      }
    );
  }

  applyFilters(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }
}
