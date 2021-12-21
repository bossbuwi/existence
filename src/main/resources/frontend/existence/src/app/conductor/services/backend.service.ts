import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, Subject } from 'rxjs';

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

  private initialize(): void {
    this.logger.logVerbose(this.className, "initialize", "Initializing service.");
    this.serverError = new RestError();
    this.loginSub = new Subject<number>();
    this.logger.logVerbose(this.className, "initialize", "Initialization complete.");
  }

  getServerError(): RestError {
    return this.serverError;
  }

  subLoginStatus(): Observable<number> {
    return this.loginSub.asObservable();
  }

  postLogin(user: User): void {
    this.loginSub.next(RequestStatus.PENDING);
    this.logger.logVerbose(this.className, "postLogin", user);
    this.logger.logVerbose(this.className, "postLogin", "Initiating server communications.");
    this.http.post<User>(RestURI.LOGIN, { user }).subscribe({
      next:
        data => {
          if (data) {
            this.logger.logVerbose(this.className, "postLogin", "Data received from server.");
            console.log(data);
            this.loginSub.next(RequestStatus.OK);
          } else {
            this.logger.logVerbose(this.className, "postLogin", "Server reply unknown.");
            this.loginSub.next(RequestStatus.ERROR);
          }
        },
      error: error => {
          this.serverError = error.error;
          this.logger.logVerbose(this.className, "postLogin", "Server replied with an error.");
          this.logger.logVerbose(this.className, "postLogin", "Server time: " + this.serverError.timestamp);
          this.logger.logVerbose(this.className, "postLogin", "Error code: " + this.serverError.status);
          this.logger.logVerbose(this.className, "postLogin", "Server message: " + this.serverError.message);
          this.loginSub.next(RequestStatus.ERROR);
        },
      complete: () => {
          this.logger.logVerbose(this.className, "postLogin", "Server communications complete.");
        }
    });
  }

  postLogout(user: User): void {
    this.loginSub.next(RequestStatus.PENDING);
    this.logger.logVerbose(this.className, "postLogout", user);
    this.logger.logVerbose(this.className, "postLogout", "Initiating server communications.");
    this.http.post<boolean>(RestURI.LOGOUT, user).subscribe({
      next:
        data => {
          if (data) {
            this.logger.logVerbose(this.className, "postLogout", "Data received from server.");
            console.log(data);
            this.loginSub.next(RequestStatus.OK);
          } else {
            this.logger.logVerbose(this.className, "postLogout", "Server reply unknown.");
            this.loginSub.next(RequestStatus.ERROR);
          }
        },
      error: error => {
          this.serverError = error.error;
          this.logger.logVerbose(this.className, "postLogout", "Server replied with an error.");
          this.logger.logVerbose(this.className, "postLogout", "Server time: " + this.serverError.timestamp);
          this.logger.logVerbose(this.className, "postLogout", "Error code: " + this.serverError.status);
          this.logger.logVerbose(this.className, "postLogout", "Server message: " + this.serverError.message);
          this.loginSub.next(RequestStatus.ERROR);
        },
      complete: () => {
          this.logger.logVerbose(this.className, "postLogout", "Server communications complete.");
        }
    });
  }
}
