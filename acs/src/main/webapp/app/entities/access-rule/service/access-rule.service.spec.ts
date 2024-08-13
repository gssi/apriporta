import { TestBed } from '@angular/core/testing';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IAccessRule } from '../access-rule.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../access-rule.test-samples';

import { AccessRuleService, RestAccessRule } from './access-rule.service';

const requireRestSample: RestAccessRule = {
  ...sampleWithRequiredData,
  startDate: sampleWithRequiredData.startDate?.toJSON(),
  endDate: sampleWithRequiredData.endDate?.toJSON(),
};

describe('AccessRule Service', () => {
  let service: AccessRuleService;
  let httpMock: HttpTestingController;
  let expectedResult: IAccessRule | IAccessRule[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(AccessRuleService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a AccessRule', () => {
      const accessRule = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(accessRule).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AccessRule', () => {
      const accessRule = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(accessRule).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AccessRule', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AccessRule', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a AccessRule', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addAccessRuleToCollectionIfMissing', () => {
      it('should add a AccessRule to an empty array', () => {
        const accessRule: IAccessRule = sampleWithRequiredData;
        expectedResult = service.addAccessRuleToCollectionIfMissing([], accessRule);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(accessRule);
      });

      it('should not add a AccessRule to an array that contains it', () => {
        const accessRule: IAccessRule = sampleWithRequiredData;
        const accessRuleCollection: IAccessRule[] = [
          {
            ...accessRule,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAccessRuleToCollectionIfMissing(accessRuleCollection, accessRule);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AccessRule to an array that doesn't contain it", () => {
        const accessRule: IAccessRule = sampleWithRequiredData;
        const accessRuleCollection: IAccessRule[] = [sampleWithPartialData];
        expectedResult = service.addAccessRuleToCollectionIfMissing(accessRuleCollection, accessRule);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(accessRule);
      });

      it('should add only unique AccessRule to an array', () => {
        const accessRuleArray: IAccessRule[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const accessRuleCollection: IAccessRule[] = [sampleWithRequiredData];
        expectedResult = service.addAccessRuleToCollectionIfMissing(accessRuleCollection, ...accessRuleArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const accessRule: IAccessRule = sampleWithRequiredData;
        const accessRule2: IAccessRule = sampleWithPartialData;
        expectedResult = service.addAccessRuleToCollectionIfMissing([], accessRule, accessRule2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(accessRule);
        expect(expectedResult).toContain(accessRule2);
      });

      it('should accept null and undefined values', () => {
        const accessRule: IAccessRule = sampleWithRequiredData;
        expectedResult = service.addAccessRuleToCollectionIfMissing([], null, accessRule, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(accessRule);
      });

      it('should return initial array if no AccessRule is added', () => {
        const accessRuleCollection: IAccessRule[] = [sampleWithRequiredData];
        expectedResult = service.addAccessRuleToCollectionIfMissing(accessRuleCollection, undefined, null);
        expect(expectedResult).toEqual(accessRuleCollection);
      });
    });

    describe('compareAccessRule', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAccessRule(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareAccessRule(entity1, entity2);
        const compareResult2 = service.compareAccessRule(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareAccessRule(entity1, entity2);
        const compareResult2 = service.compareAccessRule(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareAccessRule(entity1, entity2);
        const compareResult2 = service.compareAccessRule(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
