import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Reward } from '../../../models/reward.model';
import { RewardService } from '../../../services/reward.service';
import { ConfirmService } from '../../../shared/services/confirm.service';

@Component({
  selector: 'app-reward-list',
  templateUrl: './reward-list.component.html',
  styleUrls: ['./reward-list.component.css']
})
export class RewardListComponent implements OnInit {

  rewards: Reward[] = [];
  loading = false;
  error = '';
  formationId!: number;

  constructor(
    private service: RewardService,
    private route: ActivatedRoute,
    public router: Router,  // ✅ public pour l'utiliser dans le template
    private confirmService: ConfirmService
  ) {}

  ngOnInit(): void {
  // ✅ Vérifier que le paramètre existe et n'est pas NaN
  const idParam = this.route.snapshot.paramMap.get('formationId');
  if (idParam && !isNaN(+idParam)) {
    this.formationId = +idParam;
  }
  this.loadRewards();
}

loadRewards(): void {
  this.loading = true;
  // ✅ N'envoyer formationId que s'il est valide
  const obs = this.formationId
    ? this.service.getByFormation(this.formationId)
    : this.service.getAll();

  obs.subscribe({
    next: (data) => { this.rewards = data; this.loading = false; },
    error: (err) => {
      console.error(err);
      this.error = `Erreur ${err.status}`;
      this.loading = false;
    }
  });
}

  addReward(): void {
    this.router.navigate(['/rewards/new'], {
      queryParams: { formationId: this.formationId }
    });
  }

  viewReward(id: number): void {
    this.router.navigate(['/rewards', id]);
  }

  editReward(id: number): void {
    this.router.navigate(['/rewards', id, 'edit']);
  }

  deleteReward(id: number): void {
    this.confirmService.delete(`Reward #${id}`).subscribe(confirmed => {
      if (confirmed) {
        this.service.delete(id).subscribe({
          next: () => this.loadRewards(),
          error: (err) => console.error(err)
        });
      }
    });
  }

  goBack(): void {
    this.router.navigate(['/formations', this.formationId]);
  }
}
