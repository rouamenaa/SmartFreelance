import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CourseService } from '../../../services/course.service';
import { Course } from '../../../models/course.model';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-course-list',
  templateUrl: './course-list.component.html',
  styleUrls: ['./course-list.component.css']
})
export class CourseListComponent implements OnInit {
  courses: Course[] = [];
  formationId?: number; // optionnel
  loading = false;
  error = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private courseService: CourseService
  ) { }

  ngOnInit(): void {
    // Récupérer le paramètre 'formationId' depuis l'URL s'il existe
    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      this.formationId = +idParam;
    }

    this.loadCourses();
  }

  loadCourses(): void {
    this.loading = true;
    let obs;

    if (this.formationId) {
      // Charger les cours liés à cette formation
      obs = this.courseService.getCoursesByFormation(this.formationId);
    } else {
      // Charger tous les cours
      obs = this.courseService.getAllCourses();
    }

    obs.subscribe({
      next: (data: Course[]) => {
        this.courses = data;
        this.loading = false;
      },
      error: (err: HttpErrorResponse) => { // ✅ type strict
        console.error(err);
        this.error = `Erreur ${err.status}: ${err.message}`;
        this.loading = false;
      }
    });
  }

  goToDetail(id: number): void {
    if (this.formationId) {
      this.router.navigate(['/formations', this.formationId, 'courses', id]);
    } else {
      this.router.navigate(['/courses', id]);
    }
  }

  addCourse(): void {
    if (this.formationId) {
      this.router.navigate(['/formations', this.formationId, 'courses', 'new']);
    } else {
      this.router.navigate(['/courses/new']);
    }
  }

  goBack(): void {
    if (this.formationId) {
      this.router.navigate(['/formations', this.formationId]);
    } else {
      this.router.navigate(['/']);
    }
  }
}