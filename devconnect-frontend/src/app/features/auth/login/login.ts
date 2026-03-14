import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../../core/services/auth.service';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  imports: [FormsModule, MatFormFieldModule, MatInputModule, MatButtonModule],
  templateUrl: './login.html',
  styleUrl: './login.scss',
})

export class LoginComponent {

  email = '';
  password = '';

  constructor(
    private auth: AuthService,
    private router: Router
  ) {}

  login() {
    this.auth.login(this.email, this.password)
      .subscribe(token => {

        this.auth.saveToken(token);

        console.log(token);

        const role = this.auth.getUserRole();

        if (role === 'DEVELOPER') {
          this.router.navigate(['/developer/dashboard']);
        }

        if (role === 'RECRUITER') {
          this.router.navigate(['/recruiter/search']);
        }
      });
  }
}