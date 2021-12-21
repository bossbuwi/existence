import { Injectable } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { Observable, Subject } from 'rxjs';

import { LoggerService } from 'src/app/conductor/services/logger.service';
import { BackendService } from 'src/app/conductor/services/backend.service';
import { User } from 'src/app/symphony/models/user';
import { RequestStatus } from 'src/app/conductor/constants/properties';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private className: string = "AuthService";
  private user!: User;
  private loginStatus!: Subject<number>;
  private isUserOnline!: boolean;
  private userOnlineStatus!: Subject<boolean>;

  constructor(private logger: LoggerService, private backend: BackendService) {
    this.initialize();
  }

  private initialize(): void {
    this.logger.logVerbose(this.className, "initialize", "Initializing service.");
    this.user = new User();
    this.loginStatus = new Subject<number>();
    this.isUserOnline = false;
    this.userOnlineStatus = new Subject<boolean>();
    this.watchUserStatus();
    this.logger.logVerbose(this.className, "initialize", "Initialization complete.");
  }

  authUser(formGroup: FormGroup): User {
    this.logger.logVerbose(this.className, "authUser", "Received user login from form.");
    this.loginStatus.next(RequestStatus.PENDING);
    this.logger.logVerbose(this.className, "authUser", "Extracting user details.");
    this.user = formGroup.value;
    this.backend.postLogin(this.user);
    return this.user;
  }

  logoutUser(): void {
    this.logger.logVerbose(this.className, "logoutUser", "Begin method.");
    this.backend.postLogout(this.user);
    this.logger.logVerbose(this.className, "logoutUser", "End method.");
  }

  private watchUserStatus(): void {
    this.backend.subLoginStatus().subscribe({
      next: response => {
        switch (response) {
          case RequestStatus.OK:
            this.loginStatus.next(RequestStatus.OK);
            this.changeUserStatus();
            break;
          case RequestStatus.PENDING:
            this.loginStatus.next(RequestStatus.PENDING);
            break;
          case RequestStatus.ERROR:
            this.loginStatus.next(RequestStatus.ERROR);
            break;
          case RequestStatus.DONE:
            break;
          default:
            break;
        }
      }
    })
  }

  private changeUserStatus(): void {
    this.isUserOnline = !this.isUserOnline;
    this.userOnlineStatus.next(this.isUserOnline);
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

  getToken(): String {
    return this.user.token;
  }

  getIsUserOnline(): boolean {
    return this.isUserOnline;
  }
}
