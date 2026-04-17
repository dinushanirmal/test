export interface UiAction {
  label: string;
  action: string;
}

export interface UiSchema {
  type: string;
  data: Record<string, unknown>;
  actions: UiAction[] | null;
}
