import { Component, Input } from '@angular/core';
import { DecimalPipe } from '@angular/common';

@Component({
  selector: 'app-balance-card',
  standalone: true,
  imports: [DecimalPipe],
  templateUrl: './balance-card.component.html',
  styleUrl: './balance-card.component.scss'
})
export class BalanceCardComponent {
  @Input({ required: true }) data!: Record<string, unknown>;
}
