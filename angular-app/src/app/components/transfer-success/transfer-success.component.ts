import { Component, Input } from '@angular/core';
import { DecimalPipe } from '@angular/common';

@Component({
  selector: 'app-transfer-success',
  standalone: true,
  imports: [DecimalPipe],
  templateUrl: './transfer-success.component.html',
  styleUrl: './transfer-success.component.scss'
})
export class TransferSuccessComponent {
  @Input({ required: true }) data!: Record<string, unknown>;
}
