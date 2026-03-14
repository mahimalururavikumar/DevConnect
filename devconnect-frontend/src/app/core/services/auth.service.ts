import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { jwtDecode } from "jwt-decode";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private api = 'http://localhost:8080/api/auth';

  constructor(private http: HttpClient) { }

  login(email: string, password: string): Observable<string> {
    return this.http.post(
      `${this.api}/login`,
      { email, password },
      { responseType: 'text' }
    );
  }

  saveToken(token: string) {
    localStorage.setItem('jwt', token);
  }

  getToken(): string | null {
    return localStorage.getItem('jwt');
  }

  logout() {
    localStorage.removeItem('jwt');
  }

  getUserRole(): string | null {

    const token = this.getToken();
    if (!token) return null;

    const decoded: any = jwtDecode(token);

    return decoded.role;   // role from JWT
  }

  registerDeveloper(name: string, email: string, password: string): Observable<string> {
    return this.http.post(
      `${this.api}/register/developer`,
      { name, email, password },
      { responseType: 'text' }
    );
  }

  registerRecruiter(name: string, email: string, password: string): Observable<string> {
    return this.http.post(
      `${this.api}/register/recruiter`,
      { name, email, password },
      { responseType: 'text' }
    );
  }
}