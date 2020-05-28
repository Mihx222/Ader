import {Component, OnInit, ViewChild} from '@angular/core';
import {MatTableDataSource} from "@angular/material/table";
import {OfferViewModel} from "../../model/offer/offer-view-model";
import {MatSort} from "@angular/material/sort";
import {MatPaginator} from "@angular/material/paginator";
import {ActivatedRoute} from "@angular/router";
import {OfferStatus} from "../../model/offerstatus/offer-status.enum";

@Component({
  selector: 'app-browse-offers',
  templateUrl: './browse-offers.component.html',
  styleUrls: ['./browse-offers.component.css']
})
export class BrowseOffersComponent implements OnInit {
  defaultOffer: OfferViewModel[] = [{
    advertisementFormats: [],
    advertisementReview: false,
    assigneeName: "default",
    bids: [],
    categories: [],
    compensation: "default",
    description: "default",
    expireDate: undefined,
    freeProductSample: false,
    id: 0,
    name: 'default',
    files: []
  }];
  dataSource: MatTableDataSource<OfferViewModel> = new MatTableDataSource<OfferViewModel>(this.defaultOffer);
  offers: OfferViewModel[] = this.defaultOffer;
  searchValue: string;

  columnsToDisplay = ['image', 'name', 'categories', 'status'];

  @ViewChild(MatSort, {static: true}) sort: MatSort;
  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;

  constructor(
      public activatedRoute: ActivatedRoute
  ) {
    this.offers = this.activatedRoute.snapshot.data['offers'];
  }

  ngOnInit() {
    this.dataSource = new MatTableDataSource(this.offers);
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  applyFilters(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  addFilter(filter: string) {
    this.searchValue = filter + " ";
    this.dataSource.filter = filter.trim().toLowerCase();
  }

  clearFilter() {
    this.searchValue = '';
    this.dataSource.filter = '';
  }

  getImageUrl(image: any): any {
    return 'data:' + image.type + ';base64,' + image.bytes;
  }

  isExpired(offer: any): boolean {
    let currentDate = new Date(Date.now());
    let offerExpirationDate = new Date(offer.expireDate);
    let expired: boolean = false;

    if (currentDate > offerExpirationDate) {
      expired = true;
    }
    return expired;
  }

  isAssigned(offer: any): boolean {
    return offer.offerStatus.toString() === OfferStatus[OfferStatus.ASSIGNED];
  }
}
