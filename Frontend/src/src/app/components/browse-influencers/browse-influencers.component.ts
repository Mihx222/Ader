import {Component, OnInit, ViewChild} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {UserViewModel} from "../../model/user/user-view-model";
import {MatTableDataSource} from "@angular/material/table";
import {MatSort} from "@angular/material/sort";
import {MatPaginator} from "@angular/material/paginator";

@Component({
  selector: 'app-browse-influencers',
  templateUrl: './browse-influencers.component.html',
  styleUrls: ['./browse-influencers.component.css']
})
export class BrowseInfluencersComponent implements OnInit {

  users: UserViewModel[] = [];
  dataSource: MatTableDataSource<UserViewModel>;
  searchValue: string;

  columnsToDisplay = ['email'];

  @ViewChild(MatSort, {static: true}) sort: MatSort;
  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;

  constructor(public activatedRoute: ActivatedRoute) {
    this.users = activatedRoute.snapshot.data['users'];
  }

  ngOnInit() {
    this.dataSource = new MatTableDataSource(this.users);
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
}
