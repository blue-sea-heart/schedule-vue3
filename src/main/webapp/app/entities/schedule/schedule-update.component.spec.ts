import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import dayjs from 'dayjs';
import ScheduleUpdate from './schedule-update.vue';
import ScheduleService from './schedule.service';
import { DATE_TIME_LONG_FORMAT } from '@/shared/composables/date-format';
import AlertService from '@/shared/alert/alert.service';

import UserService from '@/entities/user/user.service';
import ScheduleStatusService from '@/entities/schedule-status/schedule-status.service';
import CategoryService from '@/entities/category/category.service';
import TagService from '@/entities/tag/tag.service';

type ScheduleUpdateComponentType = InstanceType<typeof ScheduleUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const scheduleSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<ScheduleUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Schedule Management Update Component', () => {
    let comp: ScheduleUpdateComponentType;
    let scheduleServiceStub: SinonStubbedInstance<ScheduleService>;

    beforeEach(() => {
      route = {};
      scheduleServiceStub = sinon.createStubInstance<ScheduleService>(ScheduleService);
      scheduleServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          scheduleService: () => scheduleServiceStub,

          userService: () =>
            sinon.createStubInstance<UserService>(UserService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
          scheduleStatusService: () =>
            sinon.createStubInstance<ScheduleStatusService>(ScheduleStatusService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
          categoryService: () =>
            sinon.createStubInstance<CategoryService>(CategoryService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
          tagService: () =>
            sinon.createStubInstance<TagService>(TagService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('load', () => {
      beforeEach(() => {
        const wrapper = shallowMount(ScheduleUpdate, { global: mountOptions });
        comp = wrapper.vm;
      });
      it('Should convert date from string', () => {
        // GIVEN
        const date = new Date('2019-10-15T11:42:02Z');

        // WHEN
        const convertedDate = comp.convertDateTimeFromServer(date);

        // THEN
        expect(convertedDate).toEqual(dayjs(date).format(DATE_TIME_LONG_FORMAT));
      });

      it('Should not convert date if date is not present', () => {
        expect(comp.convertDateTimeFromServer(null)).toBeNull();
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(ScheduleUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.schedule = scheduleSample;
        scheduleServiceStub.update.resolves(scheduleSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(scheduleServiceStub.update.calledWith(scheduleSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        scheduleServiceStub.create.resolves(entity);
        const wrapper = shallowMount(ScheduleUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.schedule = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(scheduleServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        scheduleServiceStub.find.resolves(scheduleSample);
        scheduleServiceStub.retrieve.resolves([scheduleSample]);

        // WHEN
        route = {
          params: {
            scheduleId: `${scheduleSample.id}`,
          },
        };
        const wrapper = shallowMount(ScheduleUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.schedule).toMatchObject(scheduleSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        scheduleServiceStub.find.resolves(scheduleSample);
        const wrapper = shallowMount(ScheduleUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
