import { Component, OnInit } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { SkillRecommendationService, SkillRecommendationResponse } from '../../core/services/skill-recommendation.service';

@Component({
  selector: 'app-skill-recommendation',
  standalone: true,
  imports: [CommonModule, HttpClientModule],
  templateUrl: './skill-recommendation.component.html',
  styleUrls: ['./skill-recommendation.component.css']
})
export class SkillRecommendationComponent implements OnInit {
  recommendation: SkillRecommendationResponse | null = null;
  loading = false;
  error = '';

  constructor(private skillService: SkillRecommendationService) { }

  ngOnInit(): void { }

  generateRecommendation(): void {
    this.loading = true;
    this.error = '';
    // Mocking freelancer ID 1 for demonstration
    this.skillService.getRecommendations({ freelancerId: 1, maxResults: 3 }).subscribe({
      next: (data) => {
        this.recommendation = data;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Erreur lors de la génération des recommandations.';
        this.loading = false;
        console.error(err);
      }
    });
  }
}