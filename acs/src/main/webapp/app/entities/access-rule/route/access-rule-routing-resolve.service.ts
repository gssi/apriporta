import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAccessRule } from '../access-rule.model';
import { AccessRuleService } from '../service/access-rule.service';

const accessRuleResolve = (route: ActivatedRouteSnapshot): Observable<null | IAccessRule> => {
  const id = route.params['id'];
  if (id) {
    return inject(AccessRuleService)
      .find(id)
      .pipe(
        mergeMap((accessRule: HttpResponse<IAccessRule>) => {
          if (accessRule.body) {
            return of(accessRule.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default accessRuleResolve;
