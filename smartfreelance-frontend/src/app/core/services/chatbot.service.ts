import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface FreelancerRecommendation {
  name: string;
  skills: string[];
  rating: number;
  matchingScore: number;
}

@Injectable({
  providedIn: 'root'
})
export class ChatbotService {
  private apiUrl = 'http://localhost:8083/api/chatbot/recommend'; // Accès direct au microservice freelancer-service sur le port 8083

  constructor(private http: HttpClient) { }

  getRecommendations(message: string): Observable<FreelancerRecommendation[]> {
    return this.http.post<FreelancerRecommendation[]>(this.apiUrl, { message });
  }
}
