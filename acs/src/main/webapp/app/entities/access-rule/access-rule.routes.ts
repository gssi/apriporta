import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { AccessRuleComponent } from './list/access-rule.component';
import { AccessRuleDetailComponent } from './detail/access-rule-detail.component';
import { AccessRuleUpdateComponent } from './update/access-rule-update.component';
import AccessRuleResolve from './route/access-rule-routing-resolve.service';

const accessRuleRoute: Routes = [
  {
    path: '',
    component: AccessRuleComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AccessRuleDetailComponent,
    resolve: {
      accessRule: AccessRuleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AccessRuleUpdateComponent,
    resolve: {
      accessRule: AccessRuleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AccessRuleUpdateComponent,
    resolve: {
      accessRule: AccessRuleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default accessRuleRoute;
