import { NgModule, APP_INITIALIZER } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { ReactiveFormsModule, FormsModule  } from '@angular/forms';
import { formatDate } from '@angular/common';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';

import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

import { AppRoutingModule } from 'src/app/app-routing.module';
import { AppComponent } from 'src/app/app.component';

import { BootService } from 'src/app/conductor/services/boot.service';
import { LoggerService } from 'src/app/conductor/services/logger.service';
import { AuthService } from 'src/app/conductor/services/auth.service';
import { AuthInterceptorService } from 'src/app/conductor/interceptors/authinterceptor.service';

import { NavbarComponent } from 'src/app/symphony/components/navbar/navbar.component';
import { SettingComponent } from 'src/app/symphony/components/setting/setting.component';
import { SonataHomeComponent } from './sonata/components/sonata-home/sonata-home.component';
import { FatalerrorComponent } from './conductor/components/fatalerror/fatalerror.component';

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    SettingComponent,
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
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptorService,
      multi: true
    },
    BootService,
    LoggerService,
    AuthService
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
