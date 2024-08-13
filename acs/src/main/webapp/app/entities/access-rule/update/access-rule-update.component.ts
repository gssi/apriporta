import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { IRoom } from 'app/entities/room/room.model';
import { RoomService } from 'app/entities/room/service/room.service';
import { AccessRuleService } from '../service/access-rule.service';
import { IAccessRule } from '../access-rule.model';
import { AccessRuleFormService, AccessRuleFormGroup } from './access-rule-form.service';

@Component({
  standalone: true,
  selector: 'jhi-access-rule-update',
  templateUrl: './access-rule-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AccessRuleUpdateComponent implements OnInit {
  isSaving = false;
  accessRule: IAccessRule | null = null;

  employeesSharedCollection: IEmployee[] = [];
  roomsSharedCollection: IRoom[] = [];

  protected accessRuleService = inject(AccessRuleService);
  protected accessRuleFormService = inject(AccessRuleFormService);
  protected employeeService = inject(EmployeeService);
  protected roomService = inject(RoomService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AccessRuleFormGroup = this.accessRuleFormService.createAccessRuleFormGroup();

  compareEmployee = (o1: IEmployee | null, o2: IEmployee | null): boolean => this.employeeService.compareEmployee(o1, o2);

  compareRoom = (o1: IRoom | null, o2: IRoom | null): boolean => this.roomService.compareRoom(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ accessRule }) => {
      this.accessRule = accessRule;
      if (accessRule) {
        this.updateForm(accessRule);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const accessRule = this.accessRuleFormService.getAccessRule(this.editForm);
    if (accessRule.id !== null) {
      this.subscribeToSaveResponse(this.accessRuleService.update(accessRule));
    } else {
      this.subscribeToSaveResponse(this.accessRuleService.create(accessRule));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAccessRule>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(accessRule: IAccessRule): void {
    this.accessRule = accessRule;
    this.accessRuleFormService.resetForm(this.editForm, accessRule);

    this.employeesSharedCollection = this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(
      this.employeesSharedCollection,
      accessRule.employee,
    );
    this.roomsSharedCollection = this.roomService.addRoomToCollectionIfMissing<IRoom>(this.roomsSharedCollection, accessRule.room);
  }

  protected loadRelationshipsOptions(): void {
    this.employeeService
      .query()
      .pipe(map((res: HttpResponse<IEmployee[]>) => res.body ?? []))
      .pipe(
        map((employees: IEmployee[]) =>
          this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(employees, this.accessRule?.employee),
        ),
      )
      .subscribe((employees: IEmployee[]) => (this.employeesSharedCollection = employees));

    this.roomService
      .query()
      .pipe(map((res: HttpResponse<IRoom[]>) => res.body ?? []))
      .pipe(map((rooms: IRoom[]) => this.roomService.addRoomToCollectionIfMissing<IRoom>(rooms, this.accessRule?.room)))
      .subscribe((rooms: IRoom[]) => (this.roomsSharedCollection = rooms));
  }
}
