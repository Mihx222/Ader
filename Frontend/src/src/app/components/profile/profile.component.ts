import {Component, OnInit, ViewChild} from '@angular/core';
import {Offer} from "../../model/offer/offer";
import {OfferService} from "../../service/offer/offer.service";
import {MatTableDataSource} from "@angular/material/table";
import {MatSort} from "@angular/material/sort";
import {MatPaginator} from "@angular/material/paginator";
import {Role} from "../../model/role/role.enum";
import {UserViewModel} from "../../model/user/user-view-model";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  user: UserViewModel;
  offersUserBidOn: Offer[] = [];
  assignedOffers: Offer[] = [];
  completedOffers: Offer[] = [];
  userCreatedOffers: Offer[] = [];

  columnsToDisplay = ['name', 'author', 'viewDetails'];
  userBidDataSource: MatTableDataSource<Offer>;
  assignedOffersDataSource: MatTableDataSource<Offer>;
  completedOffersDataSource: MatTableDataSource<Offer>;
  userCreatedOffersDataSource: MatTableDataSource<Offer>;
  @ViewChild('userBidSort', {static: false}) userBidSorter: MatSort;
  @ViewChild('userBidPaginator', {static: false}) userBidPaginator: MatPaginator;
  @ViewChild('assignedOffersSort', {static: false}) assignedOffersSorter: MatSort;
  @ViewChild('assignedOffersPaginator', {static: false}) assignedOffersPaginator: MatPaginator;
  @ViewChild('completedOffersSort', {static: false}) completedOffersSorter: MatSort;
  @ViewChild('completedOffersPaginator', {static: false}) completedOffersPaginator: MatPaginator;
  @ViewChild('userCreatedOffersSort', {static: false}) userCreatedOffersSorter: MatSort;
  @ViewChild('userCreatedOffersPaginator', {static: false}) userCreatedOffersPaginator: MatPaginator;

  constructor(
      public activatedRoute: ActivatedRoute,
      public offerService: OfferService
  ) {
    this.user = this.activatedRoute.snapshot.data['user'];

    if (this.isAdmin()) {
      this.getAdminData();
    } else if (this.isUser()) {
      this.getUserData();
    } else if (this.isAdvertiser()) {
      this.getAdvertiserData();
    }
  }

  getUserData() {
    this.offerService.getOfferByUserEmail(
        JSON.parse(localStorage.getItem('current_user')).email,
        true,
        false,
        false
    ).subscribe(
        result => {
          this.offersUserBidOn = result;
          this.userBidDataSource = new MatTableDataSource<Offer>(this.offersUserBidOn);
          this.userBidDataSource.paginator = this.userBidPaginator;
          this.userBidDataSource.sort = this.userBidSorter;
        },
        error => {
          console.log(error);
        }
    );

    this.offerService.getOfferByUserEmail(
        JSON.parse(localStorage.getItem('current_user')).email,
        false,
        true,
        false
    ).subscribe(
        result => {
          this.assignedOffers = result;
          this.assignedOffersDataSource = new MatTableDataSource<Offer>(this.assignedOffers);
          this.assignedOffersDataSource.paginator = this.assignedOffersPaginator;
          this.assignedOffersDataSource.sort = this.assignedOffersSorter;
        },
        error => {
          console.log(error);
        }
    )

    this.offerService.getOfferByUserEmail(
        JSON.parse(localStorage.getItem('current_user')).email,
        false,
        false,
        true
    ).subscribe(
        result => {
          this.completedOffers = result;
          this.completedOffersDataSource = new MatTableDataSource<Offer>(this.completedOffers);
          this.completedOffersDataSource.paginator = this.completedOffersPaginator;
          this.completedOffersDataSource.sort = this.completedOffersSorter;
        },
        error => {
          console.log(error);
        }
    )
  }

  getAdvertiserData() {
    this.offerService.getOfferByUserEmail(
        JSON.parse(localStorage.getItem('current_user')).email,
        false,
        false,
        false
    ).subscribe(
        result => {
          this.userCreatedOffers = result;
          this.userCreatedOffersDataSource = new MatTableDataSource<Offer>(this.userCreatedOffers);
          this.userCreatedOffersDataSource.paginator = this.userCreatedOffersPaginator;
          this.userCreatedOffersDataSource.sort = this.userCreatedOffersSorter;
        },
        error => {
          console.log(error);
        }
    )
  }

  getAdminData() {
    this.getUserData();
    this.getAdvertiserData();
  }

  hasRole(role: Role): boolean {
    let result = false;
    if (localStorage.getItem("current_user") === null) return false;

    JSON.parse(localStorage.getItem("current_user")).roles.forEach(_role => {
      if (_role.name === Role[role]) result = true;
    });
    return result;
  }

  isAdvertiser(): boolean {
    return this.hasRole(Role.ROLE_ADVERTISER);
  }

  isUser(): boolean {
    return this.hasRole(Role.ROLE_USER)
  }

  isAdmin(): boolean {
    return this.hasRole(Role.ROLE_ADMIN)
  }

  ngOnInit() {
  }
}
