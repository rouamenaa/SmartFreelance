import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { forkJoin, of } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatError } from '@angular/material/form-field';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatListModule } from '@angular/material/list';
import { ActivatedRoute, Router } from '@angular/router';
import { FreelancerProfile } from '../../models/freelancer-profile.model';
import { AuthService } from '../../core/serviceslogin/auth.service';
import {
  FreelancerService,
  ProfileAnalyticsDTO,
  ProfileCompletionDTO,
  ProfileViewNotificationDTO,
  SkillRecommendationDTO,
  SkillRecommendationResponseDTO
} from '../../services/freelancer-profile';
import { NotificationBellComponent } from '../../shared/components/notification-bell/notification-bell.component';

@Component({
  standalone: true,
  selector: 'app-freelancer-profile',
  templateUrl: './freelancer-profile.html',
  styleUrls: ['./freelancer-profile.css'],
  imports: [
    CommonModule, FormsModule, ReactiveFormsModule,
    MatCardModule, MatButtonModule, MatIconModule,
    MatFormFieldModule, MatInputModule, MatSelectModule,
    MatTooltipModule, MatError, MatProgressBarModule, MatListModule,
    NotificationBellComponent
  ]
})
export class FreelancerProfileComponent implements OnInit {

  profileForm!: FormGroup;
  existingProfile: FreelancerProfile | null = null;
  isEditMode: boolean = false;
  /** Freelancer profile being viewed (from route `profil-freelancer/:userId` or default 1). */
  currentUserId: number = 1;
  showForm: boolean = false;
  showProfileInfo: boolean = true;

  viewNotifications: ProfileViewNotificationDTO[] = [];
  loadingViewNotifications = false;

  successMessage: string = '';
  errorMessage: string = '';

  // Advanced Insights Data
  analytics: ProfileAnalyticsDTO | null = null;
  completion: ProfileCompletionDTO | null = null;
  skillRecommendation: SkillRecommendationDTO | null = null;
  marketRecommendation: SkillRecommendationResponseDTO | null = null;
  loadingAdvanced: boolean = false;
  showAdvanced: boolean = false;
  loadingRecommendation: boolean = false;
  downloadingCv: boolean = false;

  constructor(
    private freelancerService: FreelancerService,
    private fb: FormBuilder,
    private router: Router,
    private route: ActivatedRoute,
    private authService: AuthService
  ) { }

  ngOnInit() {
    this.initializeForm();
    this.route.paramMap.subscribe((params) => {
      const idParam = params.get('userId');
      if (idParam != null) {
        const parsed = parseInt(idParam, 10);
        this.currentUserId = Number.isNaN(parsed) ? 1 : parsed;
      } else {
        this.currentUserId = 1;
      }
      this.loadProfile();
    });
  }

  /** Dev-only: set `localStorage.debugFreelancerUserId` if JWT has no numeric id. */
  private getViewerUserId(): number | null {
    const fromJwt = this.authService.getUserId();
    if (fromJwt != null) return fromJwt;
    const dbg = typeof localStorage !== 'undefined' ? localStorage.getItem('debugFreelancerUserId') : null;
    if (!dbg) return null;
    const n = parseInt(dbg, 10);
    return Number.isNaN(n) ? null : n;
  }

  isViewingOwnProfile(): boolean {
    const v = this.getViewerUserId();
    return v != null && v === this.currentUserId;
  }

  private initializeForm() {
    this.profileForm = this.fb.group({
      firstName: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(50)]],
      lastName: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(50)]],
      title: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(80)]],
      overview: ['', [Validators.maxLength(1000)]],
      hourlyRate: [null, [Validators.required, Validators.min(5), Validators.max(300)]],
      experienceLevel: ['', [Validators.required]],
      availability: ['', [Validators.required]],
      country: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(60)]]
    });
  }

  loadProfile() {
    this.freelancerService.getById(this.currentUserId).subscribe({
      next: (data: FreelancerProfile) => {
        this.existingProfile = data;
        this.profileForm.patchValue(data);
        this.isEditMode = true;
        this.showForm = false;
        // Auto-load advanced features once profile is loaded
        this.loadAdvancedFeatures();
        this.afterProfileLoadedForViewTracking();
      },
      error: (err: any) => {
        if (err.status === 404) {
          this.isEditMode = false;
          this.showForm = false;
        } else {
          this.errorMessage = 'Erreur lors du chargement du profil.';
        }
      }
    });
  }

  private afterProfileLoadedForViewTracking() {
    const viewerId = this.getViewerUserId();
    if (viewerId != null && viewerId !== this.currentUserId) {
      this.freelancerService.recordProfileView(this.currentUserId, viewerId).subscribe({
        next: () => {},
        error: (e) => console.warn('Profile view notification skipped', e)
      });
    }
    if (this.isViewingOwnProfile()) {
      this.loadViewNotifications();
    } else {
      this.viewNotifications = [];
    }
  }

  loadViewNotifications() {
    this.loadingViewNotifications = true;
    this.freelancerService.getViewNotifications(this.currentUserId).subscribe({
      next: (list) => {
        this.viewNotifications = list;
        this.loadingViewNotifications = false;
      },
      error: () => {
        this.loadingViewNotifications = false;
      }
    });
  }

  // ─── Advanced Features Loader ────────────────────────────────
  loadAdvancedFeatures() {
    this.loadingAdvanced = true;
    this.showAdvanced = false;

    forkJoin({
      analytics: this.freelancerService.getAnalytics(this.currentUserId).pipe(catchError(err => {
        console.error('❌ Analytics Error', err);
        return of(null);
      })),
      completion: this.freelancerService.getCompletion(this.currentUserId).pipe(catchError(err => {
        console.error('❌ Completion Error', err);
        return of(null);
      }))
    }).subscribe({
      next: (results) => {
        this.analytics = results.analytics;
        this.completion = results.completion;

        this.loadingAdvanced = false;
        this.showAdvanced = true;
      },
      error: (err) => {
        console.error('❌ Global Advanced Loading Error', err);
        this.loadingAdvanced = false;
        this.showAdvanced = true; // Still show section even if some failed
      }
    });
  }

  // ─── Helpers for UI ─────────────────────────────────────────
  getCompletionColor(): string {
    const p = this.completion?.percentage ?? 0;
    if (p >= 80) return '#22c55e';
    if (p >= 50) return '#f59e0b';
    return '#ef4444';
  }

  getCompletionLabel(): string {
    const p = this.completion?.percentage ?? 0;
    if (p === 100) return 'Perfect ✨';
    if (p >= 80) return 'Almost there!';
    if (p >= 50) return 'Good progress';
    return 'Just getting started';
  }

  getScoreClass(): string {
    const s = this.marketRecommendation?.compatibilityGlobalPercent ?? 0;
    if (s >= 75) return 'score-high';
    if (s >= 40) return 'score-mid';
    return 'score-low';
  }

  getLevelIcon(level: string): string {
    switch (level?.toLowerCase()) {
      case 'expert': return 'military_tech';
      case 'advanced': return 'workspace_premium';
      case 'intermediate': return 'grade';
      default: return 'emoji_events';
    }
  }

  generateMarketRecommendation() {
    if (!this.existingProfile) {
      this.errorMessage = 'Veuillez d abord creer un profil.';
      return;
    }

    this.loadingRecommendation = true;
    this.errorMessage = '';

    this.freelancerService.getMarketSkillRecommendation({
      freelancerId: this.currentUserId,
      maxResults: 3
    }).subscribe({
      next: (result) => {
        this.marketRecommendation = result;
        this.loadingRecommendation = false;
      },
      error: (err) => {
        console.error('❌ Market Recommendation Error', err);
        this.errorMessage = 'Erreur lors de la generation des recommandations IA.';
        this.loadingRecommendation = false;
      }
    });
  }

  downloadCv() {
    this.downloadingCv = true;
    this.errorMessage = '';

    this.freelancerService.downloadCv(this.currentUserId).subscribe({
      next: (blob: Blob) => {
        const fileURL = window.URL.createObjectURL(blob);
        const anchor = document.createElement('a');
        anchor.href = fileURL;
        anchor.download = `freelancer-cv-${this.currentUserId}.pdf`;
        anchor.click();
        window.URL.revokeObjectURL(fileURL);
        this.downloadingCv = false;
      },
      error: () => {
        this.errorMessage = 'Erreur lors du telechargement du CV.';
        this.downloadingCv = false;
      }
    });
  }

  // ─── EXISTING METHODS ────────────────────────────────────────
  save() {
    this.successMessage = '';
    this.errorMessage = '';
    if (this.profileForm.invalid) {
      this.errorMessage = 'Veuillez corriger les erreurs dans le formulaire.';
      return;
    }
    const formData = this.profileForm.value;
    if (this.isEditMode) {
      this.freelancerService.update(this.currentUserId, formData).subscribe({
        next: (res: FreelancerProfile) => {
          this.existingProfile = res;
          this.successMessage = 'Profil mis à jour avec succès !';
          this.hideForm();
          this.loadAdvancedFeatures();
        },
        error: () => { this.errorMessage = 'Erreur lors de la mise à jour.'; }
      });
    } else {
      this.freelancerService.add(this.currentUserId, formData).subscribe({
        next: (res: FreelancerProfile) => {
          this.existingProfile = res;
          this.isEditMode = true;
          this.successMessage = 'Profil créé avec succès !';
          this.hideForm();
          this.loadAdvancedFeatures();
        },
        error: () => { this.errorMessage = 'Erreur lors de la création du profil.'; }
      });
    }
  }

  showUpdateForm() {
    if (this.existingProfile) {
      this.profileForm.patchValue(this.existingProfile);
      this.isEditMode = true;
    } else {
      this.profileForm.reset();
      this.isEditMode = false;
    }
    this.showForm = true;
    this.showProfileInfo = false;
    this.successMessage = '';
    this.errorMessage = '';
  }

  hideForm() {
    this.showForm = false;
    this.showProfileInfo = true;
    this.successMessage = '';
    this.errorMessage = '';
  }

  resetForm() {
    this.profileForm.reset();
    this.isEditMode = false;
  }

  resetField(fieldName: string) {
    const fieldValue = this.profileForm.get(fieldName)?.value;
    const existingValue = this.existingProfile?.[fieldName as keyof FreelancerProfile];
    if (fieldValue !== existingValue && existingValue !== undefined) {
      this.profileForm.get(fieldName)?.setValue(existingValue);
    } else {
      this.profileForm.get(fieldName)?.setValue('');
    }
    this.profileForm.get(fieldName)?.markAsTouched();
  }

  navigateToSkills() { this.router.navigate(['/skills']); }
  navigateToPortfolio() { this.router.navigate(['/portfolio']); }
}