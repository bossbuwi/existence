import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { SettingNavtabsComponent } from 'src/app/symphony/components/setting/setting-navtabs/setting-navtabs.component';
import { SettingDisplayComponent } from 'src/app/symphony/components/setting/setting-display/setting-display.component';
import { SettingSysconfigComponent } from 'src/app/symphony/components/setting/setting-sysconfig/setting-sysconfig.component';
import { SettingUsersComponent } from 'src/app/symphony/components/setting/setting-users/setting-users.component';
import { SonataNavtabsComponent } from 'src/app/sonata/components/sonata-navtabs/sonata-navtabs.component';
import { SonataSystemsComponent } from 'src/app/sonata/components/sonata-systems/sonata-systems.component';
import { SonataCalendarComponent } from 'src/app/sonata/components/sonata-calendar/sonata-calendar.component';
import { SonataEventComponent } from 'src/app/sonata/components/sonata-event/sonata-event.component';
import { SonataReportsComponent } from 'src/app/sonata/components/sonata-reports/sonata-reports.component';

const routes: Routes = [
  { path: 'home',   redirectTo: '', pathMatch: 'full' },
  { path: 'settings', component: SettingNavtabsComponent, data: {title: 'Settings'},
    children: [
      { path: '', component: SettingSysconfigComponent, data: {title: 'Settings - System Configuration'} },
      { path: 'systemconfig', component: SettingSysconfigComponent, data: {title: 'Settings - System Configuration'} },
      { path: 'users', component: SettingUsersComponent, data: {title: 'Settings - Users'} },
      { path: 'display', component: SettingDisplayComponent, data: {title: 'Settings - Display'} }
    ]
  },
  { path: 'sonata', component: SonataNavtabsComponent, data: {title: 'Sonata'},
    children: [
      { path: 'systems', component: SonataSystemsComponent, data: {title: 'Sonata - Systems'} },
      { path: 'calendar', component: SonataCalendarComponent, data: {title: 'Sonata - Calendar'} },
      { path: 'new-event', component: SonataEventComponent, data: {title: 'Sonata - New Event'} },
      { path: 'reports', component: SonataReportsComponent, data: {title: 'Sonata - Reports'} },
    ]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
