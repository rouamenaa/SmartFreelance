import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';

import { AppComponent } from './app.component';
import { LayoutComponent } from './core/layout/layout.component';
import { NavbarComponent } from './core/navbar/navbar.component';
import { SidebarComponent } from './core/sidebar/sidebar.component';
import { ConfirmDialogComponent } from './shared/components/confirm-dialog/confirm-dialog.component';
import { LoginComponent } from './auth/login/login.component';
import { UtilisateurComponent } from './utilisateur/utilisateur.component';
import { UnauthorizedComponent } from './auth/unauthorized/unauthorized.component';

@NgModule({
  declarations: [], // 
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    
    AppComponent,
    LayoutComponent,
    NavbarComponent,
    SidebarComponent,
    ConfirmDialogComponent,
    LoginComponent,
    UtilisateurComponent,
    UnauthorizedComponent
  ],
  providers: [],
  bootstrap: []
})
export class AppModule { }