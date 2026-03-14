export interface SearchResponse {
  name: string;
  experienceYears: number;
  skills: string[];
  matchScore: number;
  finalScore: number;
  githubFollowers: number;
  githubRepos: number;
}