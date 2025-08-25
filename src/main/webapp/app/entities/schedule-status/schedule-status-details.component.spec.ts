import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import ScheduleStatusDetails from './schedule-status-details.vue';
import ScheduleStatusService from './schedule-status.service';
import AlertService from '@/shared/alert/alert.service';

type ScheduleStatusDetailsComponentType = InstanceType<typeof ScheduleStatusDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const scheduleStatusSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('ScheduleStatus Management Detail Component', () => {
    let scheduleStatusServiceStub: SinonStubbedInstance<ScheduleStatusService>;
    let mountOptions: MountingOptions<ScheduleStatusDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      scheduleStatusServiceStub = sinon.createStubInstance<ScheduleStatusService>(ScheduleStatusService);

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
          scheduleStatusService: () => scheduleStatusServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        scheduleStatusServiceStub.find.resolves(scheduleStatusSample);
        route = {
          params: {
            scheduleStatusId: `${123}`,
          },
        };
        const wrapper = shallowMount(ScheduleStatusDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.scheduleStatus).toMatchObject(scheduleStatusSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        scheduleStatusServiceStub.find.resolves(scheduleStatusSample);
        const wrapper = shallowMount(ScheduleStatusDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
