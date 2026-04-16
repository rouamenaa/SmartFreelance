import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { FreelancerProfile } from '../models/freelancer-profile.model';

// ─── DTOs ────────────────────────────────────────────────────
export interface ProfileAnalyticsDTO {
  totalSkills: number;
  totalProjects: number;
  hourlyRate: number;
  experienceInYears: number;
}

export interface ProfileCompletionDTO {
  percentage: number;
  missingFields: string[];
}

export interface SkillRecommendationDTO {
  dominantSkill: string;
  topSkills: string[];
  globalSkillScore: number;
}

export interface SkillRecommendationRequestDTO {
  freelancerId: number;
  maxResults: number;
}

export interface SkillOpportunityDTO {
  skill: string;
  demandCount: number;
  freelancerCount: number;
  avgBudget: number;
  opportunityScore: number;
  compatibilityPercent: number;
  opportunityBoostPercent: number;
  aiMessage: string;
}

export interface SkillRecommendationResponseDTO {
  summaryMessage: string;
  compatibilityGlobalPercent: number;
  recommendations: SkillOpportunityDTO[];
}

export interface ProfileViewNotificationDTO {
  id: number;
  profileOwnerId: number;
  viewerUserId: number;
  viewerDisplayName: string;
  message: string;
  viewedAt: string;
}

@Injectable({ providedIn: 'root' })
export class FreelancerService {

  private baseUrl = 'http://localhost:8080/freelancer-api/profile';

  constructor(private http: HttpClient) { }

  // ─── PROFILE ─────────────────────────────────────────────
  getById(userId: number): Observable<FreelancerProfile> {
    return this.http.get<FreelancerProfile>(`${this.baseUrl}/${userId}`);
  }

  add(userId: number, profile: any): Observable<FreelancerProfile> {
    return this.http.put<FreelancerProfile>(`${this.baseUrl}/${userId}`, profile);
  }

  update(userId: number, profile: any): Observable<FreelancerProfile> {
    return this.http.put<FreelancerProfile>(`${this.baseUrl}/${userId}`, profile);
  }

  // ─── ADVANCED FEATURES ───────────────────────────────────
  getAnalytics(userId: number): Observable<ProfileAnalyticsDTO> {
    return this.http.get<ProfileAnalyticsDTO>(`${this.baseUrl}/${userId}/analytics`);
  }

  getCompletion(userId: number): Observable<ProfileCompletionDTO> {
    return this.http.get<ProfileCompletionDTO>(`${this.baseUrl}/${userId}/completion`);
  }

  getSkillRecommendation(userId: number): Observable<SkillRecommendationDTO> {
    return this.http.get<SkillRecommendationDTO>(`${this.baseUrl}/${userId}/skill-recommendation`);
  }

  getMarketSkillRecommendation(request: SkillRecommendationRequestDTO): Observable<SkillRecommendationResponseDTO> {
    return this.http.post<SkillRecommendationResponseDTO>(
      'http://localhost:8080/freelancer-api/skills/recommend',
      request
    );
  }

  downloadCv(userId: number): Observable<Blob> {
    return this.http.get(`${this.baseUrl}/${userId}/cv`, { responseType: 'blob' });
  }

  recordProfileView(profileOwnerId: number, viewerUserId: number): Observable<ProfileViewNotificationDTO | null> {
    return this.http.post<ProfileViewNotificationDTO>(
      `${this.baseUrl}/${profileOwnerId}/views`,
      { viewerUserId },
      { observe: 'response' }
    ).pipe(
      map((res) => (res.status === 204 || res.body == null ? null : res.body))
    );
  }

  getViewNotifications(profileOwnerId: number): Observable<ProfileViewNotificationDTO[]> {
    return this.http.get<ProfileViewNotificationDTO[]>(
      `${this.baseUrl}/${profileOwnerId}/view-notifications`
    );
  }
}