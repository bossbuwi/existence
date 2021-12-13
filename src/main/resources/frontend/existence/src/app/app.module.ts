import { NgModule, APP_INITIALIZER } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { ReactiveFormsModule, FormsModule  } from '@angular/forms';
import { formatDate } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

import { BootService } from 'src/app/conductor/services/boot.service';
import {LoggerService } from 'src/app/conductor/services/logger.service';

import { NavbarComponent } from './symphony/components/navbar/navbar.component';
import { SonataHomeComponent } from './sonata/components/sonata-home/sonata-home.component';
import { FatalerrorComponent } from './conductor/components/fatalerror/fatalerror.component';

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    SonataHomeComponent,
    FatalerrorComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    NgbModule,
    ReactiveFormsModule,
    FormsModule,
    HttpClientModule
  ],
  providers: [
    {
      provide: APP_INITIALIZER,
      useFactory: initializeApp,
      multi: true,
      deps: [ BootService ]
    },
    BootService,
    LoggerService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }

export function initializeApp(boot: BootService) {
  logger('initializeApp', 'Executing APP_INITIALIZER.');
  logger('initializeApp', 'Delegating startup to BootService.');
  let promise: Promise<boolean> = boot.startup();
  return () => promise;
}

function logger(methodName: string, message: string):void {
  let locale: string = 'en-US';
  let date:string = formatDate(new Date, "yyyy-MMM-dd HH:mm:ss.SSS", locale);
  let logEvent: string = '[' + date + '] ' + 'AppModule' + '.' + methodName + '(): ' +
    message;
  console.debug(logEvent);
}
