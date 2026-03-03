import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ProjectPhase } from '../models/project-phase.model';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ProjectPhaseService {
  private apiUrl = `${environment.apiUrl}/phases`;

  constructor(private http: HttpClient) {}

  getPhasesByProject(projectId: number): Observable<ProjectPhase[]> {
    return this.http.get<ProjectPhase[]>(`${this.apiUrl}/project/${projectId}`);
  }

  updatePhase(id: number, phase: ProjectPhase): Observable<ProjectPhase> {
    return this.http.put<ProjectPhase>(`${this.apiUrl}/${id}`, phase);
  }

  deletePhase(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  createPhase(phase: ProjectPhase): Observable<ProjectPhase> {
    return this.http.post<ProjectPhase>(this.apiUrl, phase);
  }
  getPhaseById(id: number): Observable<ProjectPhase> {
  return this.http.get<ProjectPhase>(`${this.apiUrl}/${id}`);
}
}
