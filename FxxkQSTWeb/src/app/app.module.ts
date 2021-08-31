import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { Unit2LoginComponent } from './unit2/unit2-login/unit2-login.component';
import { Unit2RouteComponent } from './unit2/unit2-route/unit2-route.component';
import {ReactiveFormsModule} from "@angular/forms";
import {HttpClientModule} from "@angular/common/http";
import { Unit2RegisterComponent } from './unit2/unit2-register/unit2-register.component';
import { Unit2IndexComponent } from './unit2/unit2-index/unit2-index.component';
import { Unit2AddComponent } from './unit2/unit2-add/unit2-add.component';

@NgModule({
  declarations: [
    AppComponent,
    Unit2LoginComponent,
    Unit2RouteComponent,
    Unit2RegisterComponent,
    Unit2IndexComponent,
    Unit2AddComponent
  ],
  imports: [
    HttpClientModule,
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
