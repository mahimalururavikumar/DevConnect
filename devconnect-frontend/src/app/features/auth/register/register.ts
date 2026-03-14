import { Component } from '@angular/core';
import { AuthService } from '../../../core/services/auth.service';
import { FormsModule } from '@angular/forms';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import {MatButtonModule} from '@angular/material/button';
import {MatSelectModule} from '@angular/material/select';


@Component({
  selector: 'app-register',
  imports: [ FormsModule, MatFormFieldModule, MatInputModule, MatButtonModule, MatSelectModule],
  templateUrl: './register.html',
  styleUrl: './register.scss',
})
export class Register {

  name = '';
  email = '';
  password = '';
  role = 'DEVELOPER';

  constructor(private auth: AuthService) {}

  register() {

    if (this.role === 'DEVELOPER') {
      this.auth.registerDeveloper(this.name, this.email, this.password)
        .subscribe(res => alert(res));
    }

    if (this.role === 'RECRUITER') {
      this.auth.registerRecruiter(this.name, this.email, this.password)
        .subscribe(res => alert(res));
    }
  }
}
