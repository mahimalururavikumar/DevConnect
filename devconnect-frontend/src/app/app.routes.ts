import { Routes } from '@angular/router';
import { LoginComponent } from './features/auth/login/login';
import { Register } from './features/auth/register/register';
import { Dashboard } from './features/developer/dashboard/dashboard';
import { Search } from './features/recruiter/search/search';

export const routes: Routes = [
    { path: 'login', component: LoginComponent },
    { path: 'register', component: Register },
    { path: 'developer/dashboard', component: Dashboard },
    { path: 'recruiter/search', component: Search },
    { path: '', redirectTo: 'login', pathMatch: 'full' }
];