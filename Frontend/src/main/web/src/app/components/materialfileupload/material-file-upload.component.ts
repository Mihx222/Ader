import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {animate, state, style, transition, trigger} from "@angular/animations";
import {of, Subscription} from "rxjs";
import {HttpClient, HttpErrorResponse, HttpEventType, HttpRequest} from "@angular/common/http";
import {catchError, last, map, tap} from "rxjs/operators";

@Component({
  selector: 'app-material-file-upload',
  templateUrl: './material-file-upload.component.html',
  styleUrls: ['./material-file-upload.component.css'],
  animations: [
    trigger('fadeInOut', [
      state('in', style({opacity: 100})),
      transition('* => void', [
        animate(300, style({opacity: 0}))
      ])
    ])
  ]
})
export class MaterialFileUploadComponent implements OnInit {
  /** Button text */
  @Input() text = 'Upload';
  /** Name used in form which will be sent in HTTP request. */
  @Input() param = 'imageFile';
  /** Target URL for file uploading. */
  @Input() target = 'http://localhost:8080/rest/offerimage/upload';
  /** File extension that accepted, same as 'accept' of <input type="file" />.
   By the default, it's set to 'image/*'. */
  @Input() accept = 'image/*';
  /** Allow you to add handler after its completion. Bubble up response text from remote. */
  @Output() complete = new EventEmitter<string>();
  /** Max number of files allowed to upload */
  maxFileUploadNumber: number = 10;
  /** Count the amount of files uploaded */
  uploadedFileCount: number = 0;
  /** File queue for uploading */
  files: Array<FileUploadModel> = [];

  constructor(private _http: HttpClient) {
  }

  ngOnInit() {
  }

  onClick() {
    if (this.uploadedFileCount >= this.maxFileUploadNumber) {
      alert("You cannot upload more than " + this.maxFileUploadNumber + " images!");
      return;
    }

    const fileUpload = document.getElementById('fileUpload') as HTMLInputElement;
    fileUpload.onchange = () => {
      if (fileUpload.files.length + this.uploadedFileCount > this.maxFileUploadNumber) {
        alert("You cannot upload more than " + this.maxFileUploadNumber + " images!");
        return;
      }

      for (let index = 0; index < fileUpload.files.length; index++) {
        const file = fileUpload.files[index];
        this.files.push({
          data: file, state: 'in',
          inProgress: false, progress: 0, canRetry: false, canCancel: true
        });
      }
      this.uploadFiles();
    };
    fileUpload.click();
  }

  cancelFile(file: FileUploadModel) {
    file.sub.unsubscribe();
    this.removeFileFromArray(file);
  }

  retryFile(file: FileUploadModel) {
    this.uploadFile(file);
    file.canRetry = false;
  }

  private uploadFile(file: FileUploadModel) {
    /*  FormData API provides methods and properties
        to allow easy preparation of form data to be sent with POST HTTP requests.*/
    const formData = new FormData();
    formData.append(this.param, file.data, file.data.name);

    const req = new HttpRequest(
      'POST',
      this.target + '?access_token=' + JSON.parse(localStorage.getItem('token')).access_token,
      formData,
      {
        reportProgress: true
      });

    file.inProgress = true;
    file.sub = this._http.request(req).pipe(
      map(event => {
        switch (event.type) {
          case HttpEventType.UploadProgress:
            file.progress = Math.round(event.loaded * 100 / event.total);
            break;
          case HttpEventType.Response:
            return event;
        }
      }),
      tap(message => {
      }),
      last(),
      catchError((error: HttpErrorResponse) => {
        file.inProgress = false;
        file.canRetry = true;
        return of(`${file.data.name} upload failed.`);
      })
    ).subscribe(
      (event: any) => {
        if (typeof (event) === 'object') {
          this.removeFileFromArray(file);
          this.complete.emit(event.body);
        }
      }
    );
  }

  private uploadFiles() {
    const fileUpload = document.getElementById('fileUpload') as HTMLInputElement;
    fileUpload.value = '';

    this.files.forEach(file => {
      this.uploadFile(file);
    });
  }

  private removeFileFromArray(file: FileUploadModel) {
    const index = this.files.indexOf(file);
    if (index > -1) {
      this.uploadedFileCount++;
      this.files.splice(index, 1);
    }
  }
}

export class FileUploadModel {
  data: File;
  state: string;
  inProgress: boolean;
  progress: number;
  canRetry: boolean;
  canCancel: boolean;
  sub?: Subscription;
}
