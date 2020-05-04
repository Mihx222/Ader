import {Injectable} from '@angular/core';
import {AuthService} from "../auth/auth.service";
import {HttpClient} from "@angular/common/http";
import {Category} from "../../model/category/category";

@Injectable({
  providedIn: 'root'
})
export class CategoryService {

  userURL = this.authService.BASE_URL + '/';

  constructor(private authService: AuthService, private http: HttpClient) {
  }

  getCategories() {
    return this.http.get<Category[]>(this.userURL + 'category?access_token=' +
        JSON.parse(localStorage.getItem('token')).access_token);
  }
}
