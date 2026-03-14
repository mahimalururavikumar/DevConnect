export interface SearchRequest {
  requiredSkills: string[];
  minExperience?: number;
  page: number;
  size: number;
}