import { NgModule, APP_INITIALIZER } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { ReactiveFormsModule, FormsModule  } from '@angular/forms';
import { formatDate } from '@angular/common';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';

import { AppRoutingModule } from 'src/app/app-routing.module';
import { AppComponent } from 'src/app/app.component';

import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

import { BootService } from 'src/app/conductor/services/boot.service';
import { LoggerService } from 'src/app/conductor/services/logger.service';
import { AuthService } from 'src/app/conductor/services/auth.service';
import { AuthInterceptorService } from 'src/app/conductor/interceptors/authinterceptor.service';

import { NavbarComponent } from 'src/app/symphony/components/navbar/navbar.component';
import { SonataHomeComponent } from './sonata/components/sonata-home/sonata-home.component';
import { SettingNavtabsComponent } from './symphony/components/setting/setting-navtabs/setting-navtabs.component';
import { SettingSysconfigComponent } from './symphony/components/setting/setting-sysconfig/setting-sysconfig.component';
import { SettingUsersComponent } from './symphony/components/setting/setting-users/setting-users.component';
import { SettingDisplayComponent } from './symphony/components/setting/setting-display/setting-display.component';
import { SonataNavtabsComponent } from './sonata/components/sonata-navtabs/sonata-navtabs.component';
import { SonataCalendarComponent } from './sonata/components/sonata-calendar/sonata-calendar.component';
import { SonataEventComponent } from './sonata/components/sonata-event/sonata-event.component';

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    SonataHomeComponent,
    SettingNavtabsComponent,
    SettingSysconfigComponent,
    SettingUsersComponent,
    SettingDisplayComponent,
    SonataNavtabsComponent,
    SonataCalendarComponent,
    SonataEventComponent
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
