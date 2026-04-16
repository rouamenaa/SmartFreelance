import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface SkillRecommendationRequest {
  freelancerId: number;
  maxResults?: number;
}

export interface SkillOpportunity {
  skill: string;
  demandCount: number;
  freelancerCount: number;
  avgBudget: number;
  opportunityScore: number;
  compatibilityPercent: number;
  opportunityBoostPercent: number;
  aiMessage: string;
}

export interface SkillRecommendationResponse {
  summary: string;
  compatibilityGlobal: number;
  recommendations: SkillOpportunity[];
}

@Injectable({
  providedIn: 'root'
})
export class SkillRecommendationService {
  private apiUrl = 'http://localhost:8083/api/skills';

  constructor(private http: HttpClient) {}

  getRecommendations(request: SkillRecommendationRequest): Observable<SkillRecommendationResponse> {
    return this.http.post<SkillRecommendationResponse>(this.apiUrl + '/recommend', request);
  }
}