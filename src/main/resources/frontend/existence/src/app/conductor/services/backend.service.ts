import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, Subject, Subscription } from 'rxjs';
import { FormGroup } from '@angular/forms';

import { LoggerService } from 'src/app/conductor/services/logger.service';
import { User } from 'src/app/symphony/models/user';
import { RestURI } from 'src/app/conductor/constants/resturi';
import { RestError } from 'src/app/conductor/models/error';
import { RequestStatus } from 'src/app/conductor/constants/properties';

@Injectable({
  providedIn: 'root'
})
export class BackendService {
  private className: string = "BackendService";
  private loginSub!: Subject<number>;
  private serverError! : RestError;

  constructor(private http: HttpClient, private logger: LoggerService) {
    this.initialize();
  }

  private initialize() {
    this.logger.logVerbose(this.className, "initialize", "Initializing service.");
    this.serverError = new RestError();
    this.loginSub = new Subject<number>();
    this.logger.logVerbose(this.className, "initialize", "Initialization complete.");
  }

  getServerError(): RestError {
    return this.serverError;
  }

  subLogin(): Observable<number> {
    return this.loginSub.asObservable();
  }

  login(formGroup: FormGroup) {
    this.loginSub.next(RequestStatus.PENDING);
    let user: User = formGroup.value;
    this.logger.logVerbose(this.className, "login", user);
    this.logger.logVerbose(this.className, "login", "Initiating server communications.");
    return this.http.post<User>(RestURI.LOGIN, { user }).subscribe({
      next:
        data => {
          if (data) {
            this.logger.logVerbose(this.className, "login", "Data received from server.");
            console.log(data);
            this.loginSub.next(RequestStatus.OK);
          } else {
            this.logger.logVerbose(this.className, "login", "Server reply unknown.");
            this.loginSub.next(RequestStatus.ERROR);
          }
        },
      error: error => {
        this.serverError = error.error;
        this.logger.logVerbose(this.className, "login", "Server replied with an error.");
        this.logger.logVerbose(this.className, "login", "Server time: " + this.serverError.timestamp);
        this.logger.logVerbose(this.className, "login", "Error code: " + this.serverError.status);
        this.logger.logVerbose(this.className, "login", "Server message: " + this.serverError.message);
        this.loginSub.next(RequestStatus.ERROR);
      },
      complete: () => {
        this.logger.logVerbose(this.className, "login", "Server communications complete.");
      }
    });
  }
}
