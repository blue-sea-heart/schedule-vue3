import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import InAppNotificationDetails from './in-app-notification-details.vue';
import InAppNotificationService from './in-app-notification.service';
import AlertService from '@/shared/alert/alert.service';

type InAppNotificationDetailsComponentType = InstanceType<typeof InAppNotificationDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const inAppNotificationSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('InAppNotification Management Detail Component', () => {
    let inAppNotificationServiceStub: SinonStubbedInstance<InAppNotificationService>;
    let mountOptions: MountingOptions<InAppNotificationDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      inAppNotificationServiceStub = sinon.createStubInstance<InAppNotificationService>(InAppNotificationService);

      alertService = new AlertService({
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          'font-awesome-icon': true,
          'router-link': true,
        },
        provide: {
          alertService,
          inAppNotificationService: () => inAppNotificationServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        inAppNotificationServiceStub.find.resolves(inAppNotificationSample);
        route = {
          params: {
            inAppNotificationId: `${123}`,
          },
        };
        const wrapper = shallowMount(InAppNotificationDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.inAppNotification).toMatchObject(inAppNotificationSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        inAppNotificationServiceStub.find.resolves(inAppNotificationSample);
        const wrapper = shallowMount(InAppNotificationDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
