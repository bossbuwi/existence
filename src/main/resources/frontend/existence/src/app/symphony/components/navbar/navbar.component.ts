import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms';

import { LoggerService } from 'src/app/conductor/services/logger.service';
import { AuthService } from 'src/app/conductor/services/auth.service';
import { RequestStatus } from 'src/app/conductor/constants/properties';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {
  private className: string = "NavbarComponent";
  fatalError!: boolean;
  isMenuCollapsed: boolean = true;
  loginStatus: number = RequestStatus.NONE;
  loginForm!: FormGroup;
  private userOnline!: boolean;


  constructor(private logger: LoggerService, private auth: AuthService) { }

  ngOnInit(): void {
    this.initialize();
  }

  private initialize() {
    this.logger.logVerbose(this.className, "initialize", "Initializing component.");
    this.loginForm = this.createLoginFormGroup();
    this.userOnline = false;
    this.watchLoginStatus();
    this.logger.logVerbose(this.className, "initialize", "Initialization complete.");
  }

  private createLoginFormGroup(): FormGroup {
    this.logger.logVerbose(this.className, "createLoginFormGroup", "Creating login form.");
    return new FormGroup({
      username: new FormControl(),
      password: new FormControl()
    });
  }

  onSubmit(): void {
    this.logger.logVerbose(this.className, "onSubmit",
      "User with username: " + this.loginForm.get("username")?.value + " is trying to login.");
    this.auth.authUser(this.loginForm);
  }

  logOut(): void {
    this.auth.logoutUser();
  }

  private watchLoginStatus(): void {
    this.auth.subLoginStatus().subscribe({
      next: response => {
        switch (response) {
          case RequestStatus.OK:
            if (!this.userOnline) {
              this.loginStatus = RequestStatus.OK;
              this.loginOK();
            } else {
              this.loginStatus = RequestStatus.OK;
              this.logoutOK();
            }
            break;
          case RequestStatus.PENDING:
            if (!this.userOnline) {
              this.loginStatus = RequestStatus.PENDING;
              this.loginPending();
            } else {
              this.loginStatus = RequestStatus.PENDING;
              this.logoutPending();
            }
            break;
          case RequestStatus.ERROR:
            if (!this.userOnline) {
              this.loginStatus = RequestStatus.ERROR;
              this.loginError();
            } else {
              this.loginStatus = RequestStatus.ERROR;
              this.logoutError();
            }
            break;
          case RequestStatus.DONE:
              break;
          default:
            break;
        }
      }
    });
  }

  private loginPending(): void {
    this.loginForm.disable();
  }

  private loginError(): void {
    this.loginForm.enable();
  }

  private loginOK(): void {
    this.userOnline = !this.userOnline;
    this.loginForm.reset(this.loginForm.value);
  }

  private logoutPending(): void {
    this.loginForm.disable();
  }

  private logoutError(): void {
    this.loginStatus = RequestStatus.OK;
    this.loginForm.disable();
  }

  private logoutOK(): void {
    this.userOnline = !this.userOnline;
    this.loginForm.enable();
  }

}
