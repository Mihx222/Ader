import {ActivatedRouteSnapshot, Resolve, RouterStateSnapshot} from "@angular/router";
import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {Category} from "../model/category/category";
import {CategoryService} from "../service/category/category.service";

@Injectable({
  providedIn: "root"
})
export class CategoryResolver implements Resolve<Category[]> {

  constructor(private categoryService: CategoryService) {
  }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Category[]> | Promise<Category[]> | Category[] {
    return this.categoryService.getCategories();
  }
}
