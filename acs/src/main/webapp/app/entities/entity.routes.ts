import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'acsApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'access-rule',
    data: { pageTitle: 'acsApp.accessRule.home.title' },
    loadChildren: () => import('./access-rule/access-rule.routes'),
  },
  {
    path: 'employee',
    data: { pageTitle: 'acsApp.employee.home.title' },
    loadChildren: () => import('./employee/employee.routes'),
  },
  {
    path: 'room',
    data: { pageTitle: 'acsApp.room.home.title' },
    loadChildren: () => import('./room/room.routes'),
  },
  {
    path: 'tag',
    data: { pageTitle: 'acsApp.tag.home.title' },
    loadChildren: () => import('./tag/tag.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
