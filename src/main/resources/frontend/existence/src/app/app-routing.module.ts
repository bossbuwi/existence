import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { SonataHomeComponent } from "src/app/sonata/components/sonata-home/sonata-home.component";
import { FatalerrorComponent } from './conductor/components/fatalerror/fatalerror.component';

const routes: Routes = [
  { path: 'sonata', component: SonataHomeComponent, data: {title: 'Sonata — Home'} },
  { path: 'fatalerror', component: FatalerrorComponent, data: {title: 'Fatal Error'} },
  // { path: '',   redirectTo: '/sonata', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
