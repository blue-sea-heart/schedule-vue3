import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import ScheduleDetails from './schedule-details.vue';
import ScheduleService from './schedule.service';
import AlertService from '@/shared/alert/alert.service';

type ScheduleDetailsComponentType = InstanceType<typeof ScheduleDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const scheduleSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('Schedule Management Detail Component', () => {
    let scheduleServiceStub: SinonStubbedInstance<ScheduleService>;
    let mountOptions: MountingOptions<ScheduleDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      scheduleServiceStub = sinon.createStubInstance<ScheduleService>(ScheduleService);

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
          scheduleService: () => scheduleServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        scheduleServiceStub.find.resolves(scheduleSample);
        route = {
          params: {
            scheduleId: `${123}`,
          },
        };
        const wrapper = shallowMount(ScheduleDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.schedule).toMatchObject(scheduleSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        scheduleServiceStub.find.resolves(scheduleSample);
        const wrapper = shallowMount(ScheduleDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
