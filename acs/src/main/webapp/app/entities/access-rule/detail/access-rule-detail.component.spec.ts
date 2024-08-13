import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { AccessRuleDetailComponent } from './access-rule-detail.component';

describe('AccessRule Management Detail Component', () => {
  let comp: AccessRuleDetailComponent;
  let fixture: ComponentFixture<AccessRuleDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AccessRuleDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: AccessRuleDetailComponent,
              resolve: { accessRule: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(AccessRuleDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AccessRuleDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load accessRule on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', AccessRuleDetailComponent);

      // THEN
      expect(instance.accessRule()).toEqual(expect.objectContaining({ id: 123 }));
    });
  });

  describe('PreviousState', () => {
    it('Should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
