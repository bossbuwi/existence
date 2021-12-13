import { Injectable } from '@angular/core';
import { formatDate } from '@angular/common';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Subject, Subscription } from 'rxjs';

import { RequestStatus } from 'src/app/conductor/constants/properties';
import { Setting } from 'src/app/symphony/models/setting';
import { RestURI } from 'src/app/conductor/constants/resturi';
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

  constructor(private http: HttpClient) {
    this.initialize();
  }

  private initialize() {
    this.logger("initialize", "Initializing service.");
    this.bootStatus = new Subject<number>();
    this.serverCheckStatus = new Subject<number>();
    this.logLevelStatus = new Subject<number>();
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
          this.logger('startupChain', 'Sending boot success confirmation.');
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
          this.logger('startupChain', 'Attempting to fetch frontend logging level setting.');
          this.fetchLogLevel();
        } else {
          this.logger('startupChain', 'Server check encountered an error.');
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

  private fetchLogLevel(): void {
    this.logger('fetchLogLevel', "Checking server availability.");
    this.http.get<Setting>(RestURI.GET_INIT_LOGLEVEL).subscribe({
      next: data => {
        if (data) {
          this.logger('fetchLogLevel', "Data received.");
          this.logLevel = data;
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
