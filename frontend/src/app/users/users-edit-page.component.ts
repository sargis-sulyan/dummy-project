import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { UsersService } from '../services/users.service';

@Component({
  standalone: true,
  selector: 'app-users-edit-page',
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './users-edit-page.component.html'
})
export class UsersEditPageComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly users = inject(UsersService);

  form = this.fb.nonNullable.group({
    name: ['', [Validators.required, Validators.maxLength(100)]],
    email: ['', [Validators.required, Validators.email, Validators.maxLength(200)]],
  });

  id!: number;
  loading = true;
  error: string | null = null;

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    this.id = Number(idParam);
    if (!this.id) {
      this.error = 'Invalid user id';
      this.loading = false;
      return;
    }
    this.users.getUser(this.id).subscribe({
      next: (u) => {
        this.form.patchValue(u);
        this.loading = false;
      },
      error: () => {
        this.error = 'User not found';
        this.loading = false;
      }
    });
  }

  save(): void {
    if (this.form.invalid) return;
    this.users.updateUser(this.id, this.form.getRawValue()).subscribe({
      next: () => this.router.navigate(['/users']),
      error: (e) => this.error = 'Update failed'
    });
  }
}
