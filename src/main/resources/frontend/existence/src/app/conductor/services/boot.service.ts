import { Injectable } from '@angular/core';
import { formatDate } from '@angular/common';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, Subject, Subscription } from 'rxjs';

import { RequestStatus } from 'src/app/conductor/constants/properties';
import { Setting } from 'src/app/symphony/models/setting';
import { RestURI } from 'src/app/conductor/constants/resturi';
import { Constellations } from 'src/app/conductor/constants/constellations';
import { Existence } from 'src/app/conductor/models/existence';
import { RestError } from 'src/app/conductor/models/error';

@Injectable({
  providedIn: 'root'
})
export class BootService {
  private className: string = 'BootService';
  private appStatus!: number;
  private serverError!: RestError;
  private bootStatus!: Subject<number>;
  private logLevel!: Setting;
  private backendDetails!: Existence;
  private serverCheckStatus!: Subject<number>;
  private logLevelStatus!: Subject<number>;
  private validateConstellationsStatus!: Subject<number>;
  private constellations!: string[];
  private logLevelSub!: Subject<Setting>;

  constructor(private http: HttpClient) {
    this.initialize();
  }

  // the http methods need a rework
  // pattern it after backend's calendar methods
  private initialize() {
    this.logger("initialize", "Initializing service.");
    this.bootStatus = new Subject<number>();
    this.serverCheckStatus = new Subject<number>();
    this.logLevelStatus = new Subject<number>();
    this.validateConstellationsStatus = new Subject<number>();
    this.logLevelSub = new Subject<Setting>();
    this.serverError = new RestError();
    this.backendDetails = new Existence();
    this.logLevel = new Setting();
    this.logger("initialize", "Initialization complete.");
  }

  private logger(methodName: string, message: string):void {
    let locale: string = 'en-US';
    let date:string = formatDate(new Date, "yyyy-MMM-dd HH:mm:ss.SSS", locale);
    let logEvent: string = '[' + date + '] ' + this.className + '.' + methodName + '(): ' +
      message;
    console.debug(logEvent);
  }

  startup(): Promise<boolean> {
    let promise = new Promise<boolean>((resolve, reject) => {
      this.startupChain();
      this.bootStatus.subscribe({
        next: status => {
          if (status === RequestStatus.DONE) {
            this.logger('startup', 'Startup done.');
            this.logger('startup', 'Promise is kept.');
            this.appStatus = RequestStatus.OK
            resolve(true);
          } else {
            this.logger('startup', 'Startup done.');
            this.logger('startup', 'Promise is broken.');
            window.document.body.classList.add('failed');
            reject(false);
          }
        }
      });
    });

    return promise;
  }

  private startupChain(): void {
    this.logger('startupChain', 'Commencing startup chain.');
    this.logger('startupChain', 'Hooking subscription to the last link on the chain.');
    this.logLevelStatus.subscribe({
      next: data => {
        if (data === RequestStatus.DONE) {
          this.logger('startupChain', 'Startup chain executed successfully.');
          this.logger('startupChain', 'Deleting subscriptions.');
          serverCheck.unsubscribe();
          logLevel.unsubscribe();
          validateConstellations.unsubscribe();
          this.logger('startupChain', 'Broadcasting boot success confirmation.');
          this.bootStatus.next(RequestStatus.DONE);
        }
      }
    });

    this.logger('startupChain', 'Commencing serverCheck.');
    this.serverCheck();
    let serverCheck: Subscription = this.serverCheckStatus.subscribe({
      next: data => {
        if (data === RequestStatus.DONE) {
          this.logger('startupChain', 'Server check complete.');
          this.logger('startupChain', 'Attempting to initiate validation of constellations.');
          this.validateConstellations();
        } else {
          this.logger('startupChain', 'Server check encountered an error.');
          this.bootStatus.next(RequestStatus.ERROR);
        }
      }
    });

    let validateConstellations: Subscription = this.validateConstellationsStatus.subscribe({
      next: data => {
        if (data === RequestStatus.DONE) {
          this.logger('startupChain', 'Validation of constellations complete.');
          this.logger('startupChain', 'Attempting to fetch frontend logging level setting.');
          this.fetchLogLevel();
        } else {
          this.logger('startupChain', 'A constellation error has been detected.');
          this.bootStatus.next(RequestStatus.ERROR);
        }
      }
    });

    let logLevel: Subscription = this.logLevelStatus.subscribe({
      next: data => {
        if (data === RequestStatus.DONE) {
          this.logger('startupChain', 'Logging level received.');
          this.logLevelStatus.next(RequestStatus.DONE);
        } else {
          this.logger('startupChain', 'Fetching log level encountered an error.');
          this.bootStatus.next(RequestStatus.ERROR);
        }
      }
    });
  }

  private serverCheck(): void {
    this.logger('serverCheck', "Checking server availability.");
    this.http.get<Existence>(RestURI.GET_INIT_HANDSHAKE).subscribe({
      next: data => {
        if (data) {
          this.logger('serverCheck', "Data received.");
          this.backendDetails = data;
          this.serverCheckStatus.next(RequestStatus.DONE);
        } else {
          this.logger('serverCheck', "Server reply unknown.");
          this.serverCheckStatus.next(RequestStatus.ERROR);
        }
      },
      error: error => {
        this.logger('serverCheck', "A server error occured.");
        this.serverError = error;
        console.error(this.serverError);
        this.serverCheckStatus.next(RequestStatus.ERROR);
      }
    });
  }

  private validateConstellations(): void {
    this.logger('validateConstellations', "Checking for constellation mismatch.");
    let hasError: boolean = false;
    this.constellations = Constellations.CONSTELLATIONS;
    let _constellations: string[] = this.backendDetails.constellations;

    (this.constellations.length !== _constellations.length) ? hasError = true : hasError = false;

    if (!hasError) {
      for (let i = 0; i < this.constellations.length; i++) {
        if (this.constellations[i] !== _constellations[i]) {
          this.logger('validateConstellations', "Constellation mismatch detected.");
          this.logger('validateConstellations', "Aborting validation process.");
          hasError = true;
          break;
        }
      }
    }

    if (hasError) {
      this.validateConstellationsStatus.next(RequestStatus.ERROR);
    } else {
      this.validateConstellationsStatus.next(RequestStatus.DONE);
    }
  }

  private fetchLogLevel(): void {
    this.logger('fetchLogLevel', "Checking server availability.");
    this.http.get<Setting>(RestURI.GET_INIT_LOGLEVEL).subscribe({
      next: data => {
        if (data) {
          this.logger('fetchLogLevel', "Data received.");
          this.logLevel = data;
          this.logLevelSub.next(this.logLevel);
          this.logLevelStatus.next(RequestStatus.DONE);
        } else {
          this.logger('fetchLogLevel', "Server reply unknown.");
          this.logLevelStatus.next(RequestStatus.ERROR);
        }
      },
      error: error => {
        this.logger('fetchLogLevel', "A server error occured.");
        this.serverError = error;
        console.error(this.serverError);
        this.logLevelStatus.next(RequestStatus.ERROR);
      }
    });
  }

  subLogLevel(): Observable<Setting> {
    return this.logLevelSub.asObservable();
  }

  getAppStatus(): number {
    return this.appStatus;
  }

  getLogLevel(): Setting {
    return this.logLevel;
  }

  getBackendDetails(): Existence {
    return this.backendDetails;
  }
}
