import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {CustomErrorStateMatcher} from "../../helpers/custom-error-state-matcher";
import {Offer} from "../../model/offer/offer";
import {FormControl} from "@angular/forms";
import {Bid} from "../../model/bid/bid";
import {BidService} from "../../service/bid/bid.service";
import {Role} from "../../model/role/role.enum";
import {Observable} from "rxjs";
import {MatTableDataSource} from "@angular/material/table";
import {BidViewModel} from "../../model/bid/bid-view-model";
import {MatSort} from "@angular/material/sort";
import {MatPaginator} from "@angular/material/paginator";
import {SelectionModel} from "@angular/cdk/collections";
import {animate, state, style, transition, trigger} from "@angular/animations";

@Component({
  selector: 'app-offer-page',
  templateUrl: './offer-page.component.html',
  styleUrls: ['./offer-page.component.css'],
  animations: [
    trigger('detailExpand', [
      state('collapsed', style({height: '0px', minHeight: '0'})),
      state('expanded', style({height: '*'})),
      transition('expanded <=> collapsed', animate('225ms cubic-bezier(0.4, 0.0, 0.2, 1)')),
    ]),
  ]
})
export class OfferPageComponent implements OnInit, AfterViewInit {
  dataSource: MatTableDataSource<BidViewModel>;
  bids: BidViewModel[] = [];
  searchValue: string;
  columnsToDisplay = ['select', 'author', 'compensation'];
  @ViewChild(MatSort, {static: false}) sort: MatSort;
  @ViewChild(MatPaginator, {static: false}) paginator: MatPaginator;
  matcher = new CustomErrorStateMatcher();
  whoIsYourAudience = new FormControl();
  whatDoYouDo = new FormControl();
  whatAreYouSelling = new FormControl();
  coreValues = new FormControl();
  personalityOfYourBrand = new FormControl();
  compensation = new FormControl({value: '', disabled: false});
  requirementsAccepted: boolean = false;
  freeProductSample: boolean = false;
  offer: Offer;
  offerImages: any[] = [];
  offerExpirationDate;
  currentDate;
  timeToDisplay;
  offerExpired: boolean = false;
  newBid: Bid;
  existingBid: Observable<Bid>;
  selection = new SelectionModel<BidViewModel>(true, []);
  expandedElement: BidViewModel | null;

  constructor(
      public activatedRoute: ActivatedRoute,
      public bidService: BidService
  ) {
    this.offer = this.activatedRoute.snapshot.data['offer'];
    this.bids = this.offer.bids;

    this.calculateDate();

    if (!this.isAuthor()) {
      this.existingBid = this.bidService.getBidByUserEmailAndOfferId(
          JSON.parse(localStorage.getItem("current_user")).email,
          this.offer.id);
    }
  }

  /** Whether the number of selected elements matches the total number of rows. */
  isAllSelected() {
    const numSelected = this.selection.selected.length;
    const numRows = this.dataSource.data.length;
    return numSelected === numRows;
  }

  /** Selects all rows if they are not all selected; otherwise clear selection. */
  masterToggle() {
    this.isAllSelected() ?
        this.selection.clear() :
        this.dataSource.data.forEach(row => this.selection.select(row));
  }

  /** The label for the checkbox on the passed row */
  checkboxLabel(row?: BidViewModel): string {
    if (!row) {
      return `${this.isAllSelected() ? 'select' : 'deselect'} all`;
    }
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.id + 1}`;
  }

  ngOnInit() {
    this.dataSource = new MatTableDataSource(this.bids);

    this.offer.files.forEach(file => {
      if (file.type.includes('image')) {
        this.offerImages.push({
          source: 'data:' + file.type + ';base64,' + file.bytes,
          title: file.name
        });
      }
    })
  }

  calculateDate() {
    this.currentDate = new Date(Date.now());
    this.offerExpirationDate = new Date(this.offer.expireDate);

    this.timeToDisplay = Math.round(Math.abs(this.offerExpirationDate.getTime() - this.currentDate.getTime()) / 36e5);

    if (this.currentDate > this.offerExpirationDate) {
      this.timeToDisplay = -this.timeToDisplay;
      this.offerExpired = true;
    }
  }

  checkForErrors() {
    return this.whoIsYourAudience.hasError('required') ||
        this.whatDoYouDo.hasError('required') ||
        this.whatAreYouSelling.hasError('required') ||
        this.coreValues.hasError('required') ||
        this.personalityOfYourBrand.hasError('required');
  }

  disableFields() {
    if (this.requirementsAccepted) {
      this.compensation.enable();
    } else {
      this.compensation.disable();
    }
  }

  isUser(): boolean {
    let result = false;
    JSON.parse(localStorage.getItem("current_user")).roles.forEach(role => {
      if (role.name === Role[Role.ROLE_USER]) result = true;
    });
    return result;
  }

  isAuthor(): boolean {
    let result = false;
    JSON.parse(localStorage.getItem("current_user")).createdOffers.forEach(offer => {
      if (offer.id === this.offer.id) result = true;
    })
    return result;
  }

  placeBid() {
    let newBid = {
      offerId: this.offer.id,
      userEmail: JSON.parse(localStorage.getItem("current_user")).email,
      acceptInitialRequirements: this.requirementsAccepted,
      compensation: this.requirementsAccepted ? undefined : this.compensation.value,
      freeProductSample: this.freeProductSample,
      persona: {
        activity: this.whatDoYouDo.value,
        audience: this.whoIsYourAudience.value,
        sellingOrientation: this.whatAreYouSelling.value,
        values: this.coreValues.value
      }
    }

    this.bidService.placeBid(newBid).subscribe(
        result => {
          this.newBid = result;

          // TODO: Find a way to replace this
          location.reload();
        }, error => {
          console.log(error);
        }
    );
  }

  ngAfterViewInit(): void {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }
}
