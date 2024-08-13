import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAccessRule, NewAccessRule } from '../access-rule.model';

export type PartialUpdateAccessRule = Partial<IAccessRule> & Pick<IAccessRule, 'id'>;

type RestOf<T extends IAccessRule | NewAccessRule> = Omit<T, 'startDate' | 'endDate'> & {
  startDate?: string | null;
  endDate?: string | null;
};

export type RestAccessRule = RestOf<IAccessRule>;

export type NewRestAccessRule = RestOf<NewAccessRule>;

export type PartialUpdateRestAccessRule = RestOf<PartialUpdateAccessRule>;

export type EntityResponseType = HttpResponse<IAccessRule>;
export type EntityArrayResponseType = HttpResponse<IAccessRule[]>;

@Injectable({ providedIn: 'root' })
export class AccessRuleService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/access-rules');

  create(accessRule: NewAccessRule): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(accessRule);
    return this.http
      .post<RestAccessRule>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(accessRule: IAccessRule): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(accessRule);
    return this.http
      .put<RestAccessRule>(`${this.resourceUrl}/${this.getAccessRuleIdentifier(accessRule)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(accessRule: PartialUpdateAccessRule): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(accessRule);
    return this.http
      .patch<RestAccessRule>(`${this.resourceUrl}/${this.getAccessRuleIdentifier(accessRule)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestAccessRule>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestAccessRule[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAccessRuleIdentifier(accessRule: Pick<IAccessRule, 'id'>): number {
    return accessRule.id;
  }

  compareAccessRule(o1: Pick<IAccessRule, 'id'> | null, o2: Pick<IAccessRule, 'id'> | null): boolean {
    return o1 && o2 ? this.getAccessRuleIdentifier(o1) === this.getAccessRuleIdentifier(o2) : o1 === o2;
  }

  addAccessRuleToCollectionIfMissing<Type extends Pick<IAccessRule, 'id'>>(
    accessRuleCollection: Type[],
    ...accessRulesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const accessRules: Type[] = accessRulesToCheck.filter(isPresent);
    if (accessRules.length > 0) {
      const accessRuleCollectionIdentifiers = accessRuleCollection.map(accessRuleItem => this.getAccessRuleIdentifier(accessRuleItem));
      const accessRulesToAdd = accessRules.filter(accessRuleItem => {
        const accessRuleIdentifier = this.getAccessRuleIdentifier(accessRuleItem);
        if (accessRuleCollectionIdentifiers.includes(accessRuleIdentifier)) {
          return false;
        }
        accessRuleCollectionIdentifiers.push(accessRuleIdentifier);
        return true;
      });
      return [...accessRulesToAdd, ...accessRuleCollection];
    }
    return accessRuleCollection;
  }

  protected convertDateFromClient<T extends IAccessRule | NewAccessRule | PartialUpdateAccessRule>(accessRule: T): RestOf<T> {
    return {
      ...accessRule,
      startDate: accessRule.startDate?.toJSON() ?? null,
      endDate: accessRule.endDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restAccessRule: RestAccessRule): IAccessRule {
    return {
      ...restAccessRule,
      startDate: restAccessRule.startDate ? dayjs(restAccessRule.startDate) : undefined,
      endDate: restAccessRule.endDate ? dayjs(restAccessRule.endDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestAccessRule>): HttpResponse<IAccessRule> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestAccessRule[]>): HttpResponse<IAccessRule[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
