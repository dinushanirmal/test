import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AgentResponse } from '../models/agent-response.model';

@Injectable({ providedIn: 'root' })
export class AgentService {
  private http = inject(HttpClient);

  // Uses proxy.conf.json in dev — no hardcoded host needed
  sendMessage(sessionId: string, message: string): Observable<AgentResponse> {
    return this.http.post<AgentResponse>('/api/agent/message', { sessionId, message });
  }

  confirm(sessionId: string, action: string): Observable<AgentResponse> {
    return this.http.post<AgentResponse>('/api/agent/confirm', { sessionId, action });
  }
}
