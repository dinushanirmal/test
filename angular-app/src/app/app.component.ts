import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AgentService } from './services/agent.service';
import { AgentResponse } from './models/agent-response.model';
import { UiRendererComponent } from './components/ui-renderer/ui-renderer.component';

interface ChatEntry {
  role: 'user' | 'agent';
  text?: string;
  response?: AgentResponse;
}

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [FormsModule, UiRendererComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  private agentService = inject(AgentService);

  readonly sessionId = crypto.randomUUID();
  message = '';
  loading = signal(false);
  history = signal<ChatEntry[]>([]);

  send(): void {
    const text = this.message.trim();
    if (!text || this.loading()) return;

    this.message = '';
    this.history.update(h => [...h, { role: 'user', text }]);
    this.loading.set(true);

    this.agentService.sendMessage(this.sessionId, text).subscribe({
      next: response => {
        this.loading.set(false);
        this.history.update(h => [...h, { role: 'agent', response }]);
      },
      error: err => {
        this.loading.set(false);
        this.history.update(h => [...h, {
          role: 'agent',
          response: {
            uiSchema: null,
            awaitingConfirmation: false,
            cancelled: false,
            error: true,
            errorMessage: err.message ?? 'Something went wrong'
          }
        }]);
      }
    });
  }

  onAction(action: string): void {
    if (action === 'CANCEL') {
      // Optimistically show cancelled without a network call
      this.history.update(h => [...h, {
        role: 'agent',
        response: { uiSchema: null, awaitingConfirmation: false, cancelled: true, error: false, errorMessage: null }
      }]);
      return;
    }

    this.loading.set(true);
    this.agentService.confirm(this.sessionId, action).subscribe({
      next: response => {
        this.loading.set(false);
        this.history.update(h => [...h, { role: 'agent', response }]);
      },
      error: err => {
        this.loading.set(false);
        this.history.update(h => [...h, {
          role: 'agent',
          response: {
            uiSchema: null,
            awaitingConfirmation: false,
            cancelled: false,
            error: true,
            errorMessage: err.message ?? 'Confirmation failed'
          }
        }]);
      }
    });
  }

  onKeyDown(event: KeyboardEvent): void {
    if (event.key === 'Enter' && !event.shiftKey) {
      event.preventDefault();
      this.send();
    }
  }
}
