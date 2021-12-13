import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms';
import { Router } from '@angular/router';

import { BootService } from 'src/app/conductor/services/boot.service';
import { BackendService } from 'src/app/conductor/services/backend.service';
import { LoggerService } from 'src/app/conductor/services/logger.service';

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


  constructor(private boot: BootService, private backend: BackendService,
    private logger: LoggerService, private router: Router) { }

  ngOnInit(): void {
    this.logger.logVerbose(this.className, "ngOnInit", "Checking for app status.");
    if (this.boot.getAppStatus() === RequestStatus.ERROR) {
      this.fatalError = true;
      this.logger.logVerbose(this.className, "ngOnInit", this.fatalError);
      this.router.navigate(['fatalerror']);
    } else {
      this.initialize();
    }
  }

  private initialize() {
    this.logger.logVerbose(this.className, "initialize", "Initializing component.");
    this.loginForm = this.createLoginFormGroup();

    this.backend.subLogin().subscribe({
      next: response => {
        switch (response) {
          case RequestStatus.OK:
            this.loginStatus = RequestStatus.OK;
            this.loginOK();
            break;
          case RequestStatus.PENDING:
            this.loginStatus = RequestStatus.PENDING;
            this.loginPending();
            break;
          case RequestStatus.ERROR:
            this.loginStatus = RequestStatus.ERROR;
            this.loginError();
            break;
          case RequestStatus.DONE:
              break;
          default:
            this.loginStatus = RequestStatus.NONE;

            break;
        }
      }
    });

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
    this.logger.logVerbose(this.className, "onSubmit", "User with username: " + this.loginForm.get("username")?.value
      + " is trying to login.");
    this.backend.login(this.loginForm);
  }

  private loginPending(): void {
    this.loginForm.disable();
  }

  private loginError(): void {
    this.loginForm.enable();
  }

  private loginOK(): void {
    this.loginForm.enable();
  }

}
