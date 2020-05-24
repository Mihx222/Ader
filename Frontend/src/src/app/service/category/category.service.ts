import {Injectable} from '@angular/core';
import {AuthService} from "../auth/auth.service";
import {HttpClient} from "@angular/common/http";
import {Category} from "../../model/category/category";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class CategoryService {

  categoryURL = this.authService.BASE_URL + '/';

  constructor(private authService: AuthService, private http: HttpClient) {
  }

  getCategories() {
    return this.http.get<Category[]>(this.categoryURL + 'category?access_token=' +
        JSON.parse(localStorage.getItem('token')).access_token);
  }

  addCategory(category: Category): Observable<Category> {
    return this.http.post<Category>(this.categoryURL + 'category/add?access_token=' +
        JSON.parse(localStorage.getItem('token')).access_token, category);
  }

  deleteCategory(categoryName: string): Observable<string> {
    return this.http.delete(this.categoryURL + 'category/name/' + categoryName + '?access_token=' +
        JSON.parse(localStorage.getItem('token')).access_token, {responseType: "text"});
  }
}
