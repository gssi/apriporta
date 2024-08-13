import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../access-rule.test-samples';

import { AccessRuleFormService } from './access-rule-form.service';

describe('AccessRule Form Service', () => {
  let service: AccessRuleFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AccessRuleFormService);
  });

  describe('Service methods', () => {
    describe('createAccessRuleFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAccessRuleFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            notes: expect.any(Object),
            employee: expect.any(Object),
            room: expect.any(Object),
          }),
        );
      });

      it('passing IAccessRule should create a new form with FormGroup', () => {
        const formGroup = service.createAccessRuleFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            notes: expect.any(Object),
            employee: expect.any(Object),
            room: expect.any(Object),
          }),
        );
      });
    });

    describe('getAccessRule', () => {
      it('should return NewAccessRule for default AccessRule initial value', () => {
        const formGroup = service.createAccessRuleFormGroup(sampleWithNewData);

        const accessRule = service.getAccessRule(formGroup) as any;

        expect(accessRule).toMatchObject(sampleWithNewData);
      });

      it('should return NewAccessRule for empty AccessRule initial value', () => {
        const formGroup = service.createAccessRuleFormGroup();

        const accessRule = service.getAccessRule(formGroup) as any;

        expect(accessRule).toMatchObject({});
      });

      it('should return IAccessRule', () => {
        const formGroup = service.createAccessRuleFormGroup(sampleWithRequiredData);

        const accessRule = service.getAccessRule(formGroup) as any;

        expect(accessRule).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAccessRule should not enable id FormControl', () => {
        const formGroup = service.createAccessRuleFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAccessRule should disable id FormControl', () => {
        const formGroup = service.createAccessRuleFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
