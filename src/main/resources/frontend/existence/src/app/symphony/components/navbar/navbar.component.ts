import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms';

import { LoggerService } from 'src/app/conductor/services/logger.service';
import { AuthService } from 'src/app/conductor/services/auth.service';
import { RequestStatus, FormStatus } from 'src/app/conductor/constants/properties';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {
  private className: string = "NavbarComponent";
  fatalError!: boolean;
  isMenuCollapsed: boolean = true;
  loginForm!: FormGroup;
  formStatus: number = FormStatus.UNKNOWN;
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
    this.autoLogin();
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

  private autoLogin(): void {
    this.auth.autoLogin();
  }

  private watchLoginStatus(): void {
    this.auth.subLoginStatus().subscribe({
      next: response => {
        switch (response) {
          case RequestStatus.OK:
            if (!this.userOnline) {
              this.loginOK();
            } else {
              this.logoutOK();
            }
            break;
          case RequestStatus.PENDING:
            if (!this.userOnline) {
              this.loginPending();
            } else {
              this.logoutPending();
            }
            break;
          case RequestStatus.ERROR:
            if (!this.userOnline) {
              this.loginError();
            } else {
              this.logoutError();
            }
            break;
          case RequestStatus.DONE:
            this.autoLoginFailed();
            break;
          default:
            break;
        }
      }
    });
  }

  private loginPending(): void {
    this.formStatus = FormStatus.LOADING;
    this.loginForm.disable();
  }

  private loginError(): void {
    this.formStatus = FormStatus.ERROR;
    this.loginForm.enable();
  }

  private loginOK(): void {
    this.userOnline = !this.userOnline;
    this.formStatus = FormStatus.DONE;
    this.loginForm.reset();
    this.logger.logVerbose(this.className,
      "loginOK", "User with username: " + this.auth.getUser().username + " successfully logged in.");
  }

  private logoutPending(): void {
    this.formStatus = FormStatus.LOADING;
    this.loginForm.disable();
  }

  private logoutError(): void {
    this.formStatus = FormStatus.ERROR;
    this.loginForm.disable();
  }

  private logoutOK(): void {
    this.userOnline = !this.userOnline;
    this.formStatus = FormStatus.INSERT;
    this.loginForm.enable();
  }

  private autoLoginFailed(): void {
    this.formStatus = FormStatus.INSERT;
    this.loginForm.enable();
  }
}
