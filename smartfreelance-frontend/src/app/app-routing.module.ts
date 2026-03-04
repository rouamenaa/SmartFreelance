import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UnauthorizedComponent } from './auth/unauthorized/unauthorized.component';
import { UtilisateurComponent } from './utilisateur/utilisateur.component';
import { DashboardComponent } from './admin/dashboard/dashboard.component';
import { authGuard } from './core/guards/auth.guard';
import { LoginComponent } from './auth/login/login.component';



export const routes: Routes = [
  {
    path: '',
    loadChildren: () =>
      import('./features/projects/projects.module')
        .then(m => m.ProjectsModule)
  },

  {
    path: 'condidatures',
    loadChildren: () =>
      import('./features/condidature/condidature.module')
        .then(m => m.CondidatureModule)
  },
  {
    path: 'contrats',
    loadChildren: () =>
      import('./features/Contract/contract.module')
        .then(m => m.ContractModule)
  },
  {
    path: 'profil-freelancer',
    loadComponent: () =>
      import('./features/freelancer-profile/freelancer-profile')
        .then(m => m.FreelancerProfileComponent)
  },
  
  {
    path: 'portfolio',
    loadComponent: () =>
      import('./features/portfolio-project/portfolio-project')
        .then(m => m.PortfolioProjectComponent)
  },
  {
    path: 'skills',
    loadComponent: () =>
      import('./features/skill/skill')
        .then(m => m.SkillComponent)
  },
  
  { path: 'login', component: LoginComponent },

  { path: 'utilisateur', component: UtilisateurComponent },

  { path: 'admin', component: DashboardComponent, canActivate: [authGuard] },

  { path: 'unauthorized', component: UnauthorizedComponent },

  { path: '', redirectTo: 'login', pathMatch: 'full' }, 

  { path: '**', redirectTo: 'login' } 
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }