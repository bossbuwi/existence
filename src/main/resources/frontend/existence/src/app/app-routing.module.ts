import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { SettingNavtabsComponent } from 'src/app/symphony/components/setting/setting-navtabs/setting-navtabs.component';
import { SettingDisplayComponent } from 'src/app/symphony/components/setting/setting-display/setting-display.component';
import { SonataHomeComponent } from 'src/app/sonata/components/sonata-home/sonata-home.component';
import { SonataNavtabsComponent } from 'src/app/sonata/components/sonata-navtabs/sonata-navtabs.component';
import { SettingSysconfigComponent } from './symphony/components/setting/setting-sysconfig/setting-sysconfig.component';
import { SettingUsersComponent } from './symphony/components/setting/setting-users/setting-users.component';

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
      { path: 'home', component: SonataHomeComponent, data: {title: 'Sonata'} },
    ]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
