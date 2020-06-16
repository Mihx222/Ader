import {Injectable} from '@angular/core';
import {AuthService} from "../auth/auth.service";
import {HttpClient} from "@angular/common/http";
import {File} from "../../model/file/file";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class FileService {
  fileURL = this.authService.BASE_URL + '/';

  constructor(private authService: AuthService, private http: HttpClient) {
  }

  deleteFile(uuid: string) {
    return this.http.delete<void>(this.fileURL + 'file/' + uuid + '?access_token=' +
        JSON.parse(localStorage.getItem('token')).access_token);
  }

  getFilesForUser(offerIsNull: boolean): Observable<File[]> {
    return this.http.get<File[]>(
        this.fileURL + 'file/user/' +
        JSON.parse(localStorage.getItem("current_user")).email +
        '?offerIsNull=' + offerIsNull +
        '&access_token=' +
        JSON.parse(localStorage.getItem('token')).access_token
    );
  }
}
