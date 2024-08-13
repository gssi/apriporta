import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IAccessRule } from '../access-rule.model';
import { AccessRuleService } from '../service/access-rule.service';

@Component({
  standalone: true,
  templateUrl: './access-rule-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class AccessRuleDeleteDialogComponent {
  accessRule?: IAccessRule;

  protected accessRuleService = inject(AccessRuleService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.accessRuleService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
