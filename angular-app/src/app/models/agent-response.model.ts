import { UiSchema } from './ui-schema.model';

export interface AgentResponse {
  uiSchema: UiSchema | null;
  awaitingConfirmation: boolean;
  cancelled: boolean;
  error: boolean;
  errorMessage: string | null;
}
