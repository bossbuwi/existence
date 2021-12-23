import { Injectable } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { Observable, Subject } from 'rxjs';

import { LoggerService } from 'src/app/conductor/services/logger.service';
import { BackendService } from 'src/app/conductor/services/backend.service';
import { RequestStatus, AuthProps } from 'src/app/conductor/constants/properties';
import { User } from 'src/app/symphony/models/user';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private className: string = "AuthService";
  private user!: User;
  private authToken!: string;
  private loginStatus!: Subject<number>;
  private isUserOnline!: boolean;
  private userOnlineStatus!: Subject<boolean>;
  private isTokenAvailable!: boolean;

  constructor(private logger: LoggerService, private backend: BackendService) {
    this.initialize();
  }

  private initialize(): void {
    this.logger.logVerbose(this.className, "initialize", "Initializing service.");
    this.user = new User();
    this.loginStatus = new Subject<number>();
    this.isUserOnline = false;
    this.isTokenAvailable = false;
    this.userOnlineStatus = new Subject<boolean>();
    this.watchUserStatus();
    this.logger.logVerbose(this.className, "initialize", "Initialization complete.");
  }

  authUser(formGroup: FormGroup): void {
    this.logger.logVerbose(this.className, "authUser", "Received user login from view.");
    this.loginStatus.next(RequestStatus.PENDING);
    this.logger.logVerbose(this.className, "authUser", "Extracting user details.");
    this.user = formGroup.value;
    this.logger.logVerbose(this.className, "authUser", "Sending user details to backend service.");
    this.backend.postLogin(this.user);
  }

  logoutUser(): void {
    this.logger.logVerbose(this.className, "logoutUser", "Received logout request from view.");
    this.logger.logVerbose(this.className, "logoutUser", "Sending request to backend service.");
    this.backend.postLogout(this.user);
  }

  autoLogin(): void {
    this.logger.logVerbose(this.className, "autoLogin", "Received autologin request from view.");
    this.logger.logVerbose(this.className, "autoLogin", "Checking existence of token from local storage.");
    this.logger.logVerbose(this.className, "autoLogin", "Sending request to backend service.");
    this.backend.postAutologin(this.checkToken());
  }

  private watchUserStatus(): void {
    this.logger.logVerbose(this.className, "watchUserStatus", "Subscribing to user status changes from backend service.");
    this.backend.subLoginStatus().subscribe({
      next: response => {
        switch (response) {
          case RequestStatus.OK:
            this.changeUserStatus();
            this.loginStatus.next(RequestStatus.OK);
            break;
          case RequestStatus.PENDING:
            this.loginStatus.next(RequestStatus.PENDING);
            break;
          case RequestStatus.ERROR:
            this.loginStatus.next(RequestStatus.ERROR);
            break;
          case RequestStatus.DONE:
            this.loginStatus.next(RequestStatus.DONE);
            break;
          default:
            break;
        }
      }
    });
  }

  private checkToken(): string {
    this.logger.logVerbose(this.className, "checkToken", "Attempting to read for token from local storage.");
    let token: string = localStorage.getItem(AuthProps.JWT_KEY) as string
    if (token !== null) {
      this.isTokenAvailable = true;
      this.authToken = token;
      return token;
    } else {
      this.isTokenAvailable = false;
      return AuthProps.TOKEN_NULL;
    }
  }

  private setToken(): void {
    this.logger.logVerbose(this.className, "setToken", "Setting token on browser's local storage.");
    this.authToken = this.user.token;
    localStorage.setItem(AuthProps.JWT_KEY, this.user.token);
  }

  private clearToken(): void {
    this.logger.logVerbose(this.className, "clearToken", "Removing token on browser's local storage.");
    localStorage.removeItem(AuthProps.JWT_KEY);
  }

  private changeUserStatus(): void {
    this.logger.logVerbose(this.className, "changeUserStatus", "Changing user's login status.");
    this.isUserOnline = !this.isUserOnline;
    this.logger.logVerbose(this.className, "changeUserStatus", "Broadcasting user's login status to other components.");
    this.userOnlineStatus.next(this.isUserOnline);
    if (this.isUserOnline) {
      this.user = this.backend.getData();
      this.clearToken();
      this.setToken();
    } else {
      this.user = new User();
      this.clearToken();
    }
  }

  subLoginStatus(): Observable<number> {
    return this.loginStatus.asObservable();
  }

  subUserOnlineStatus(): Observable<boolean> {
    return this.userOnlineStatus.asObservable();
  }

  getUser(): User {
    return this.user;
  }

  getToken(): string {
    return this.authToken;
  }

  getIsUserOnline(): boolean {
    return this.isUserOnline;
  }

  getIsTokenAvailable(): boolean {
    return this.isTokenAvailable;
  }
}
