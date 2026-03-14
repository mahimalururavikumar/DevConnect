import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { DeveloperProfile } from '../../shared/models/developer-profile.model';

@Injectable({ providedIn: 'root' })
export class DeveloperService {

  private api = 'http://localhost:8080/api/developer';

  constructor(private http: HttpClient) {}

  createProfile(data: any) {
    return this.http.post(`${this.api}/profile`, data, { responseType: 'text' });
  }

  getProfile() {
    return this.http.get<DeveloperProfile>(`${this.api}/profile`);
  }

  addProject(project: any) {
    return this.http.post(`${this.api}/project`, project, { responseType: 'text' });
  }

  uploadResume(file: File) {

    const formData = new FormData();
    formData.append("file", file);

    return this.http.post(`${this.api}/resume`, formData, { responseType: 'text' });
  }
}