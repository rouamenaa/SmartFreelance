import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { BrowserModule, provideClientHydration } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { ProjectsModule } from './features/projects/projects.module';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

// Core
import { LayoutComponent } from './core/layout/layout.component';
import { NavbarComponent } from './core/navbar/navbar.component';
import { SidebarComponent } from './core/sidebar/sidebar.component';

// Shared
import { ConfirmDialogComponent } from './shared/components/confirm-dialog/confirm-dialog.component';

// Formation
import { FormationListComponent } from './features/formation/formation-list/formation-list.component';
import { FormationDetailComponent } from './features/formation/formation-detail/formation-detail.component';
import { FormationFormComponent } from './features/formation/formation-form/formation-form.component';
import { RegistrationDialogComponent } from './features/formation/registration-dialog/registration-dialog.component';
import { FormationStatisticsComponent } from './features/formation/formation-statistics/formation-statistics.component';

// Course
import { CourseListComponent } from './features/course/course-list/course-list.component';
import { CourseFormComponent } from './features/course/course-form/course-form.component';
import { CourseDetailComponent } from './features/course/course-detail/course-detail.component';

// Rewards
import { RewardListComponent } from './features/rewards/reward-list/reward-list.component';
import { RewardFormComponent } from './features/rewards/reward-form/reward-form.component';
import { RewardDetailComponent } from './features/rewards/reward-detail/reward-detail.component';

// Tests
import { TestListComponent } from './features/tests/test-list/test-list.component';
import { TestFormComponent } from './features/tests/test-form/test-form.component';
import { TestDetailComponent } from './features/tests/test-detail/test-detail.component';

// Angular Material
import { MatDialogModule } from '@angular/material/dialog';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatTableModule } from '@angular/material/table';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatCardModule } from '@angular/material/card';
import { MatBadgeModule } from '@angular/material/badge';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatChipsModule } from '@angular/material/chips';

// Pipes
import { CommonModule } from '@angular/common';

@NgModule({
  declarations: [
    AppComponent,

    // Core
    LayoutComponent,
    NavbarComponent,
    SidebarComponent,

    // Shared
    ConfirmDialogComponent,

    // Formation
    FormationListComponent,
    FormationDetailComponent,
    FormationFormComponent,
    RegistrationDialogComponent,
    FormationStatisticsComponent,

    // Course
    CourseListComponent,
    CourseFormComponent,
    CourseDetailComponent,

    // Rewards
    RewardListComponent,
    RewardFormComponent,
    RewardDetailComponent,

    // Tests
    TestListComponent,
    TestFormComponent,
    TestDetailComponent,
  ],

  imports: [
    // Angular Core
    BrowserModule,
    CommonModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,

    // Feature Modules
    ProjectsModule,

    // Angular Material
    MatDialogModule,
    MatSnackBarModule,
    MatTableModule,
    MatProgressBarModule,
    MatCardModule,
    MatBadgeModule,
    MatButtonModule,
    MatInputModule,
    MatFormFieldModule,
    MatIconModule,
    MatProgressSpinnerModule,
    MatTooltipModule,
    MatChipsModule,
  ],

  providers: [
    provideClientHydration(),
    provideAnimationsAsync()
  ],

  bootstrap: [AppComponent],

  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class AppModule { }