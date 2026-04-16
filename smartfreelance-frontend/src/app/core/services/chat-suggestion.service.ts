import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { map, tap } from 'rxjs/operators';

export interface SuggestionResponse {
  suggestions: string[];
}

@Injectable({
  providedIn: 'root'
})
export class ChatSuggestionService {
  private apiUrl = 'http://localhost:8083/api/chatbot/suggestions';
  private cache = new Map<string, string[]>();

  constructor(private http: HttpClient) {}

  getSuggestions(query: string): Observable<string[]> {
    const q = query.trim().toLowerCase();
    
    if (q.length < 2) {
      return of([]);
    }

    if (this.cache.has(q)) {
      return of(this.cache.get(q)!);
    }

    return this.http.get<SuggestionResponse>(`${this.apiUrl}?q=${q}`).pipe(
      map(res => res.suggestions),
      tap(suggestions => this.cache.set(q, suggestions))
    );
  }

  clearCache(): void {
    this.cache.clear();
  }
}
