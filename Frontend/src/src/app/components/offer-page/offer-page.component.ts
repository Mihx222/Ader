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
import {OfferStatus} from "../../model/offerstatus/offer-status.enum";
import {BidStatus} from "../../model/bidstatus/bid-status";
import {OfferService} from "../../service/offer/offer.service";

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
  columnsToDisplay = ['select', 'author', 'compensation', 'bidStatus'];
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
  assigned: boolean = false;
  existingBid: Observable<Bid>;
  selection = new SelectionModel<BidViewModel>(true, []);
  expandedElement: BidViewModel | null;
  deassignStatusDeclined: string = BidStatus[BidStatus.DECLINED];
  deassignStatusCanceled: string = BidStatus[BidStatus.CANCELED];

  constructor(
      public activatedRoute: ActivatedRoute,
      public bidService: BidService,
      public offerService: OfferService
  ) {
    this.offer = this.activatedRoute.snapshot.data['offer'];
    this.bids = this.offer.bids;

    if (this.offer.offerStatus.toString() === OfferStatus[OfferStatus.ASSIGNED]) this.assigned = true;
    this.calculateDate();

    if (!this.isAuthor()) {
      if (localStorage.getItem("current_user") !== null) {
        this.existingBid = this.bidService.getBidByUserEmailAndOfferId(
            JSON.parse(localStorage.getItem("current_user")).email,
            this.offer.id);
      }
    }
  }

  checkSelected() {
    return this.selection.isEmpty();
  }

  acceptBid() {
    this.bidService.acceptBids(this.selection.selected).subscribe(
        result => {
          // TODO: find a way to refresh the data without reloading the page
          location.reload();
        },
        error => {
          console.log(error);
        }
    );
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
    if (localStorage.getItem("current_user") === null) return false;

    JSON.parse(localStorage.getItem("current_user")).roles.forEach(role => {
      if (role.name === Role[Role.ROLE_USER]) result = true;
    });
    return result;
  }

  isAuthor(): boolean {
    if (localStorage.getItem("current_user") === null) return false;
    return this.offer.authorName === JSON.parse(localStorage.getItem("current_user")).brandName;
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
      },
      bidStatus: null
    }

    this.bidService.placeBid(newBid).subscribe(
        result => {
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

  bisIsNew(bid: Bid) {
    return bid.bidStatus === BidStatus[BidStatus.NEW];
  }

  bidIsDeclined(bid: Bid) {
    return bid.bidStatus === BidStatus[BidStatus.DECLINED];
  }

  bidIsCanceled(bid: Bid) {
    return bid.bidStatus === BidStatus[BidStatus.CANCELED];
  }

  deassignFromOffer(assigneeName: string, bidStatus: string): void {
    this.offerService.deassignFromOffer(assigneeName, this.offer.id.toString(), bidStatus).subscribe(
        result => {
          this.offer.assigneeNames.splice(
              this.offer.assigneeNames.findIndex(assignee => assignee === assigneeName),
              1
          );
          this.offer.bids.forEach(bid => {
            if (bid.userEmail === assigneeName) {
              bid.bidStatus = BidStatus[bidStatus];
            }
          });
          this.offer.bids.find(bid => {return bid.userEmail === assigneeName;}).bidStatus = BidStatus.DECLINED;
        },
        error => {
          console.log(error);
        }
    );
  }

  isAssignee(): boolean {
    let currentUser = JSON.parse(localStorage.getItem("current_user"));
    let result: boolean = false;

    this.offer.assigneeNames.forEach(assignee => {
      if (assignee === currentUser.email) {
        result = true;
      }
    });
    return result;
  }

  getCurrentAssigned(): string {
    let currentUser = JSON.parse(localStorage.getItem("current_user"));
    let result: string = null;

    this.offer.assigneeNames.forEach(assignee => {
      if (assignee === currentUser.email) {
        result = assignee;
      }
    });
    return result;
  }

  startOffer() {
    this.offerService.updateStatus(this.offer.id, OfferStatus.IN_PROGRESS).subscribe(
        result => {
          this.offer.offerStatus = OfferStatus.IN_PROGRESS
        },
        error => {
          console.log(error);
        }
    );
  }
}
