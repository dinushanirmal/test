import { Component, Input, Output, EventEmitter } from '@angular/core';
import { DecimalPipe } from '@angular/common';
import { UiAction } from '../../models/ui-schema.model';

@Component({
  selector: 'app-transfer-review',
  standalone: true,
  imports: [DecimalPipe],
  templateUrl: './transfer-review.component.html',
  styleUrl: './transfer-review.component.scss'
})
export class TransferReviewComponent {
  @Input({ required: true }) data!: Record<string, unknown>;
  @Input() actions: UiAction[] = [];
  @Output() actionTriggered = new EventEmitter<string>();
}
