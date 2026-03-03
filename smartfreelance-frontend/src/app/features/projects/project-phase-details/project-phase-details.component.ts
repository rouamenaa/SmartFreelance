import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ProjectPhaseService } from '../../../services/phase.service';
import { ProjectPhase } from '../../../models/project-phase.model';

@Component({
  selector: 'app-project-phase-details',
  templateUrl: './project-phase-details.component.html',
  styleUrls: ['./project-phase-details.component.css']
})
export class ProjectPhaseDetailsComponent implements OnInit {

  phase!: ProjectPhase;
  phaseId!: number;
  showTaskForm: boolean = false; 

  constructor(
    private route: ActivatedRoute,
    private phaseService: ProjectPhaseService
  ) {}

  ngOnInit(): void {
    this.phaseId = Number(this.route.snapshot.paramMap.get('id'));
    this.loadPhase();
  }

  loadPhase(): void {
  this.phaseService.getPhaseById(this.phaseId).subscribe({
    next: data => {
      this.phase = data;
      console.log("Loaded phase:", this.phase);
    },
    error: err => console.error("Error loading phase:", err)
  });
}
}