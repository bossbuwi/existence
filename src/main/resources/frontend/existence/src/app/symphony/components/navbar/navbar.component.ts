import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms';
import { Router } from '@angular/router';

import { LoggerService } from 'src/app/conductor/services/logger.service';
import { AuthService } from 'src/app/conductor/services/auth.service';
import { RequestStatus, FormStatus } from 'src/app/conductor/constants/properties';
import { Constellations } from 'src/app/conductor/constants/constellations';

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
  constellations!: string[];
  private userOnline!: boolean;


  constructor(private logger: LoggerService, private auth: AuthService,
    private router: Router) { }

  ngOnInit(): void {
    this.initialize();
  }

  private initialize() {
    this.logger.logVerbose(this.className, "initialize", "Initializing component.");
    this.loginForm = this.createLoginFormGroup();
    this.userOnline = false;
    this.constellations = Constellations.CONSTELLATIONS;
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

  onDropdownClick(index: any) {
    this.router.navigate([this.constellations[index].toLowerCase()]);
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
    this.router.navigate(['home']);
  }

  private autoLoginFailed(): void {
    this.formStatus = FormStatus.INSERT;
    this.loginForm.enable();
  }
}
