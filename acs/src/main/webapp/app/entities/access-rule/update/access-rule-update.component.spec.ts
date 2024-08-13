import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient, HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { IRoom } from 'app/entities/room/room.model';
import { RoomService } from 'app/entities/room/service/room.service';
import { IAccessRule } from '../access-rule.model';
import { AccessRuleService } from '../service/access-rule.service';
import { AccessRuleFormService } from './access-rule-form.service';

import { AccessRuleUpdateComponent } from './access-rule-update.component';

describe('AccessRule Management Update Component', () => {
  let comp: AccessRuleUpdateComponent;
  let fixture: ComponentFixture<AccessRuleUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let accessRuleFormService: AccessRuleFormService;
  let accessRuleService: AccessRuleService;
  let employeeService: EmployeeService;
  let roomService: RoomService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [AccessRuleUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(AccessRuleUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AccessRuleUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    accessRuleFormService = TestBed.inject(AccessRuleFormService);
    accessRuleService = TestBed.inject(AccessRuleService);
    employeeService = TestBed.inject(EmployeeService);
    roomService = TestBed.inject(RoomService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Employee query and add missing value', () => {
      const accessRule: IAccessRule = { id: 456 };
      const employee: IEmployee = { id: 26639 };
      accessRule.employee = employee;

      const employeeCollection: IEmployee[] = [{ id: 17912 }];
      jest.spyOn(employeeService, 'query').mockReturnValue(of(new HttpResponse({ body: employeeCollection })));
      const additionalEmployees = [employee];
      const expectedCollection: IEmployee[] = [...additionalEmployees, ...employeeCollection];
      jest.spyOn(employeeService, 'addEmployeeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ accessRule });
      comp.ngOnInit();

      expect(employeeService.query).toHaveBeenCalled();
      expect(employeeService.addEmployeeToCollectionIfMissing).toHaveBeenCalledWith(
        employeeCollection,
        ...additionalEmployees.map(expect.objectContaining),
      );
      expect(comp.employeesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Room query and add missing value', () => {
      const accessRule: IAccessRule = { id: 456 };
      const room: IRoom = { id: 25020 };
      accessRule.room = room;

      const roomCollection: IRoom[] = [{ id: 5556 }];
      jest.spyOn(roomService, 'query').mockReturnValue(of(new HttpResponse({ body: roomCollection })));
      const additionalRooms = [room];
      const expectedCollection: IRoom[] = [...additionalRooms, ...roomCollection];
      jest.spyOn(roomService, 'addRoomToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ accessRule });
      comp.ngOnInit();

      expect(roomService.query).toHaveBeenCalled();
      expect(roomService.addRoomToCollectionIfMissing).toHaveBeenCalledWith(
        roomCollection,
        ...additionalRooms.map(expect.objectContaining),
      );
      expect(comp.roomsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const accessRule: IAccessRule = { id: 456 };
      const employee: IEmployee = { id: 10670 };
      accessRule.employee = employee;
      const room: IRoom = { id: 11553 };
      accessRule.room = room;

      activatedRoute.data = of({ accessRule });
      comp.ngOnInit();

      expect(comp.employeesSharedCollection).toContain(employee);
      expect(comp.roomsSharedCollection).toContain(room);
      expect(comp.accessRule).toEqual(accessRule);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAccessRule>>();
      const accessRule = { id: 123 };
      jest.spyOn(accessRuleFormService, 'getAccessRule').mockReturnValue(accessRule);
      jest.spyOn(accessRuleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ accessRule });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: accessRule }));
      saveSubject.complete();

      // THEN
      expect(accessRuleFormService.getAccessRule).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(accessRuleService.update).toHaveBeenCalledWith(expect.objectContaining(accessRule));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAccessRule>>();
      const accessRule = { id: 123 };
      jest.spyOn(accessRuleFormService, 'getAccessRule').mockReturnValue({ id: null });
      jest.spyOn(accessRuleService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ accessRule: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: accessRule }));
      saveSubject.complete();

      // THEN
      expect(accessRuleFormService.getAccessRule).toHaveBeenCalled();
      expect(accessRuleService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAccessRule>>();
      const accessRule = { id: 123 };
      jest.spyOn(accessRuleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ accessRule });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(accessRuleService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareEmployee', () => {
      it('Should forward to employeeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(employeeService, 'compareEmployee');
        comp.compareEmployee(entity, entity2);
        expect(employeeService.compareEmployee).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareRoom', () => {
      it('Should forward to roomService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(roomService, 'compareRoom');
        comp.compareRoom(entity, entity2);
        expect(roomService.compareRoom).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
