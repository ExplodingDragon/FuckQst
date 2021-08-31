import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {Unit2RouteComponent} from "./unit2/unit2-route/unit2-route.component";
import {Unit2LoginComponent} from "./unit2/unit2-login/unit2-login.component";
import {Unit2IndexComponent} from "./unit2/unit2-index/unit2-index.component";
import {Unit2RegisterComponent} from "./unit2/unit2-register/unit2-register.component";
import {Unit2AddComponent} from "./unit2/unit2-add/unit2-add.component";

const routes: Routes = [
  {
    path: 'unit2',
    component: Unit2RouteComponent,
    children: [
      {
        path: '',
        redirectTo: 'login',
        pathMatch: 'full'

      },
      {
        path: 'login',
        component: Unit2LoginComponent
      },{
        path: 'register',
        component: Unit2RegisterComponent
      },
      {
        path: 'status',
        component: Unit2IndexComponent
      },
      {
        path: 'update/:id',
        component: Unit2AddComponent
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
