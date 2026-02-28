import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    pathMatch: 'full',
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
      import('./features/contract/contract.module')
        .then(m => m.ContractModule)
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
