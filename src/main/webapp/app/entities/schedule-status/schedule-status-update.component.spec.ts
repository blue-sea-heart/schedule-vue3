import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import ScheduleStatusUpdate from './schedule-status-update.vue';
import ScheduleStatusService from './schedule-status.service';
import AlertService from '@/shared/alert/alert.service';

type ScheduleStatusUpdateComponentType = InstanceType<typeof ScheduleStatusUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const scheduleStatusSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<ScheduleStatusUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('ScheduleStatus Management Update Component', () => {
    let comp: ScheduleStatusUpdateComponentType;
    let scheduleStatusServiceStub: SinonStubbedInstance<ScheduleStatusService>;

    beforeEach(() => {
      route = {};
      scheduleStatusServiceStub = sinon.createStubInstance<ScheduleStatusService>(ScheduleStatusService);
      scheduleStatusServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

      alertService = new AlertService({
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          'font-awesome-icon': true,
          'b-input-group': true,
          'b-input-group-prepend': true,
          'b-form-datepicker': true,
          'b-form-input': true,
        },
        provide: {
          alertService,
          scheduleStatusService: () => scheduleStatusServiceStub,
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(ScheduleStatusUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.scheduleStatus = scheduleStatusSample;
        scheduleStatusServiceStub.update.resolves(scheduleStatusSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(scheduleStatusServiceStub.update.calledWith(scheduleStatusSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        scheduleStatusServiceStub.create.resolves(entity);
        const wrapper = shallowMount(ScheduleStatusUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.scheduleStatus = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(scheduleStatusServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        scheduleStatusServiceStub.find.resolves(scheduleStatusSample);
        scheduleStatusServiceStub.retrieve.resolves([scheduleStatusSample]);

        // WHEN
        route = {
          params: {
            scheduleStatusId: `${scheduleStatusSample.id}`,
          },
        };
        const wrapper = shallowMount(ScheduleStatusUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.scheduleStatus).toMatchObject(scheduleStatusSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        scheduleStatusServiceStub.find.resolves(scheduleStatusSample);
        const wrapper = shallowMount(ScheduleStatusUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
