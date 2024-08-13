import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IAccessRule, NewAccessRule } from '../access-rule.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAccessRule for edit and NewAccessRuleFormGroupInput for create.
 */
type AccessRuleFormGroupInput = IAccessRule | PartialWithRequiredKeyOf<NewAccessRule>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IAccessRule | NewAccessRule> = Omit<T, 'startDate' | 'endDate'> & {
  startDate?: string | null;
  endDate?: string | null;
};

type AccessRuleFormRawValue = FormValueOf<IAccessRule>;

type NewAccessRuleFormRawValue = FormValueOf<NewAccessRule>;

type AccessRuleFormDefaults = Pick<NewAccessRule, 'id' | 'startDate' | 'endDate'>;

type AccessRuleFormGroupContent = {
  id: FormControl<AccessRuleFormRawValue['id'] | NewAccessRule['id']>;
  startDate: FormControl<AccessRuleFormRawValue['startDate']>;
  endDate: FormControl<AccessRuleFormRawValue['endDate']>;
  notes: FormControl<AccessRuleFormRawValue['notes']>;
  employee: FormControl<AccessRuleFormRawValue['employee']>;
  room: FormControl<AccessRuleFormRawValue['room']>;
};

export type AccessRuleFormGroup = FormGroup<AccessRuleFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AccessRuleFormService {
  createAccessRuleFormGroup(accessRule: AccessRuleFormGroupInput = { id: null }): AccessRuleFormGroup {
    const accessRuleRawValue = this.convertAccessRuleToAccessRuleRawValue({
      ...this.getFormDefaults(),
      ...accessRule,
    });
    return new FormGroup<AccessRuleFormGroupContent>({
      id: new FormControl(
        { value: accessRuleRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      startDate: new FormControl(accessRuleRawValue.startDate),
      endDate: new FormControl(accessRuleRawValue.endDate),
      notes: new FormControl(accessRuleRawValue.notes),
      employee: new FormControl(accessRuleRawValue.employee),
      room: new FormControl(accessRuleRawValue.room),
    });
  }

  getAccessRule(form: AccessRuleFormGroup): IAccessRule | NewAccessRule {
    return this.convertAccessRuleRawValueToAccessRule(form.getRawValue() as AccessRuleFormRawValue | NewAccessRuleFormRawValue);
  }

  resetForm(form: AccessRuleFormGroup, accessRule: AccessRuleFormGroupInput): void {
    const accessRuleRawValue = this.convertAccessRuleToAccessRuleRawValue({ ...this.getFormDefaults(), ...accessRule });
    form.reset(
      {
        ...accessRuleRawValue,
        id: { value: accessRuleRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AccessRuleFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      startDate: currentTime,
      endDate: currentTime,
    };
  }

  private convertAccessRuleRawValueToAccessRule(
    rawAccessRule: AccessRuleFormRawValue | NewAccessRuleFormRawValue,
  ): IAccessRule | NewAccessRule {
    return {
      ...rawAccessRule,
      startDate: dayjs(rawAccessRule.startDate, DATE_TIME_FORMAT),
      endDate: dayjs(rawAccessRule.endDate, DATE_TIME_FORMAT),
    };
  }

  private convertAccessRuleToAccessRuleRawValue(
    accessRule: IAccessRule | (Partial<NewAccessRule> & AccessRuleFormDefaults),
  ): AccessRuleFormRawValue | PartialWithRequiredKeyOf<NewAccessRuleFormRawValue> {
    return {
      ...accessRule,
      startDate: accessRule.startDate ? accessRule.startDate.format(DATE_TIME_FORMAT) : undefined,
      endDate: accessRule.endDate ? accessRule.endDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
