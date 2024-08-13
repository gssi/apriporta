import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IAccessRule } from '../access-rule.model';

@Component({
  standalone: true,
  selector: 'jhi-access-rule-detail',
  templateUrl: './access-rule-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class AccessRuleDetailComponent {
  accessRule = input<IAccessRule | null>(null);

  previousState(): void {
    window.history.back();
  }
}
