import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, Subject } from 'rxjs';

import { LoggerService } from 'src/app/conductor/services/logger.service';
import { User } from 'src/app/symphony/models/user';
import { RestURI } from 'src/app/conductor/constants/resturi';
import { RestError } from 'src/app/conductor/models/error';
import { AuthProps, RequestStatus } from 'src/app/conductor/constants/properties';
import { Setting } from 'src/app/symphony/models/setting';

@Injectable({
  providedIn: 'root'
})
export class BackendService {
  private className: string = "BackendService";
  private loginSub!: Subject<number>;
  private settingsSub!: Subject<number>;
  private serverError! : RestError;
  private data: any;

  constructor(private http: HttpClient, private logger: LoggerService) {
    this.initialize();
  }

  private initialize(): void {
    this.logger.logVerbose(this.className, "initialize", "Initializing service.");
    this.serverError = new RestError();
    this.loginSub = new Subject<number>();
    this.settingsSub = new Subject<number>();
    this.logger.logVerbose(this.className, "initialize", "Initialization complete.");
  }

  /*
   * Below are methods that allow other classes to access this service's subjects.
  */

  subLoginStatus(): Observable<number> {
    return this.loginSub.asObservable();
  }

  subSettingsStatus(): Observable<number> {
    return this.settingsSub.asObservable();
  }

  /*
   * Below are methods that are related to user authentication.
  */

  postLogin(user: User): void {
    this.logger.logVerbose(this.className, "postLogin", "Received request for user login.");
    this.loginSub.next(RequestStatus.PENDING);
    this.logger.logVerbose(this.className, "postLogin", "Initiating server communications.");
    this.http.post<User>(RestURI.LOGIN, { user }).subscribe({
      next:
        data => {
          if (data) {
            this.logger.logVerbose(this.className, "postLogin", "Data received from server.");
            this.data = data;
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
    this.logger.logVerbose(this.className, "postLogout", "Received request for user logout.");
    this.loginSub.next(RequestStatus.PENDING);
    this.logger.logVerbose(this.className, "postLogout", "Initiating server communications.");
    this.http.post<boolean>(RestURI.LOGOUT, user).subscribe({
      next:
        data => {
          if (data) {
            this.logger.logVerbose(this.className, "postLogin", "Data received from server.");
            this.data = data;
            this.loginSub.next(RequestStatus.OK);
          } else {
            this.logger.logVerbose(this.className, "postLogin", "Server reply unknown.");
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

  postAutologin(token: string): void {
    this.logger.logVerbose(this.className, "postAutologin", "Attempting automatic login.");
    this.loginSub.next(RequestStatus.PENDING);
    if (token === AuthProps.TOKEN_NULL) {
      this.logger.logVerbose(this.className, "postAutologin", "Invalid token.");
      this.logger.logVerbose(this.className, "postAutologin", "Aborting autologin process.");
      this.loginSub.next(RequestStatus.DONE);
    } else {
      this.http.post<User>(RestURI.AUTO_LOGIN, token).subscribe({
        next:
          data => {
            if (data) {
              this.logger.logVerbose(this.className, "postAutologin", "Data received from server.");
              this.data = data;
              this.loginSub.next(RequestStatus.OK);
            } else {
              this.logger.logVerbose(this.className, "postAutologin", "Server reply unknown.");
              this.loginSub.next(RequestStatus.DONE);
            }
          },
        error: error => {
            this.serverError = error.error;
            this.logger.logVerbose(this.className, "postAutologin", "Server replied with an error.");
            this.logger.logVerbose(this.className, "postAutologin", "Server time: " + this.serverError.timestamp);
            this.logger.logVerbose(this.className, "postAutologin", "Error code: " + this.serverError.status);
            this.logger.logVerbose(this.className, "postAutologin", "Server message: " + this.serverError.message);
            this.loginSub.next(RequestStatus.DONE);
          },
        complete: () => {
            this.logger.logVerbose(this.className, "postAutologin", "Server communications complete.");
          }
      });
    }
  }

  /*
   * Below are methods that are related to settings.
  */

  getSettings(): void {
    this.logger.logVerbose(this.className, "getSettings", "Sending request to server.");
    this.settingsSub.next(RequestStatus.PENDING);
    this.http.get<Setting[]>(RestURI.GET_SETTING_INDEX).subscribe({
      next:
        data => {
          if (data) {
            this.logger.logVerbose(this.className, "getSettings", "Data received from server.");
            this.data = data;
            console.log(data);
            this.settingsSub.next(RequestStatus.OK);
          } else {
            this.logger.logVerbose(this.className, "getSettings", "Server reply unknown.");
            this.settingsSub.next(RequestStatus.ERROR);
          }
        },
      error: error => {
          this.serverError = error.error;
          this.logger.logVerbose(this.className, "getSettings", "Server replied with an error.");
          this.logger.logVerbose(this.className, "getSettings", "Server time: " + this.serverError.timestamp);
          this.logger.logVerbose(this.className, "getSettings", "Error code: " + this.serverError.status);
          this.logger.logVerbose(this.className, "getSettings", "Server message: " + this.serverError.message);
          this.settingsSub.next(RequestStatus.ERROR);
        },
      complete: () => {
          this.logger.logVerbose(this.className, "getSettings", "Server communications complete.");
        }
    });
  }

  putSettings(setting: any): void {
    this.logger.logVerbose(this.className, "putSettings", "Sending request to server.");
    this.settingsSub.next(RequestStatus.PENDING);
    this.http.put<Setting>(RestURI.PUT_SETTING, { setting: setting }).subscribe({
      next:
        data => {
          if (data) {
            this.logger.logVerbose(this.className, "putSettings", "Data received from server.");
            this.data = data;
            console.log(data);
            this.settingsSub.next(RequestStatus.OK);
          } else {
            this.logger.logVerbose(this.className, "putSettings", "Server reply unknown.");
            this.settingsSub.next(RequestStatus.ERROR);
          }
          this.settingsSub.next(RequestStatus.OK);
        },
      error: error => {
          this.serverError = error.error;
          this.logger.logVerbose(this.className, "putSettings", "Server replied with an error.");
          this.logger.logVerbose(this.className, "putSettings", "Server time: " + this.serverError.timestamp);
          this.logger.logVerbose(this.className, "putSettings", "Error code: " + this.serverError.status);
          this.logger.logVerbose(this.className, "putSettings", "Server message: " + this.serverError.message);
          this.settingsSub.next(RequestStatus.ERROR);
        },
      complete: () => {
          this.logger.logVerbose(this.className, "putSettings", "Server communications complete.");
        }
    });
  }

  /*
   * Below are methods that allow other classes to access this service's private fields.
  */

  provideData(): any {
    return this.data;
  }

  provideServerError(): RestError {
    return this.serverError;
  }
}
