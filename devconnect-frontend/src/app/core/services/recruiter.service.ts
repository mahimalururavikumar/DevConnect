import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { SearchRequest } from '../../shared/models/searchRequest.model';
import { PageResponse } from '../../shared/models/pageResponse.model';
import { SearchResponse } from '../../shared/models/searchResponse.model';

@Injectable({ providedIn: 'root' })
export class RecruiterService {

  private api = 'http://localhost:8080/api/recruiter';

  constructor(private http: HttpClient) {}

  searchDevelopers(request: SearchRequest) {
    return this.http.post<PageResponse<SearchResponse>>(
      `${this.api}/search`,
      request
    );
  }
}