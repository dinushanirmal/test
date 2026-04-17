import { Component, Input, Output, EventEmitter } from '@angular/core';
import { UiSchema } from '../../models/ui-schema.model';
import { BalanceCardComponent } from '../balance-card/balance-card.component';
import { TransferReviewComponent } from '../transfer-review/transfer-review.component';
import { TransferSuccessComponent } from '../transfer-success/transfer-success.component';

@Component({
  selector: 'app-ui-renderer',
  standalone: true,
  imports: [BalanceCardComponent, TransferReviewComponent, TransferSuccessComponent],
  template: `
    @switch (schema.type) {
      @case ('balance_card') {
        <app-balance-card [data]="schema.data" />
      }
      @case ('transfer_review') {
        <app-transfer-review
          [data]="schema.data"
          [actions]="schema.actions ?? []"
          (actionTriggered)="actionTriggered.emit($event)" />
      }
      @case ('transfer_success') {
        <app-transfer-success [data]="schema.data" />
      }
      @default {
        <div class="unknown">Unknown UI type: {{ schema.type }}</div>
      }
    }
  `,
  styles: [`.unknown { padding: 12px; color: #9ca3af; font-size: 13px; }`]
})
export class UiRendererComponent {
  @Input({ required: true }) schema!: UiSchema;
  @Output() actionTriggered = new EventEmitter<string>();
}
