import { Project } from "./project.model";

export interface DeveloperProfile {
  name: string;
  bio: string;
  experienceYears: number;
  githubUsername: string;
  skills: string[];
  profileCompletionPercentage: number;
  projects: Project[];
}