import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';

import { SkillRecommendationService } from './skill-recommendation.service';

describe('SkillRecommendationService', () => {
  let service: SkillRecommendationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient()]
    });
    service = TestBed.inject(SkillRecommendationService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
