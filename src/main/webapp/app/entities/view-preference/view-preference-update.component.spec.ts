import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import dayjs from 'dayjs';
import ViewPreferenceUpdate from './view-preference-update.vue';
import ViewPreferenceService from './view-preference.service';
import { DATE_TIME_LONG_FORMAT } from '@/shared/composables/date-format';
import AlertService from '@/shared/alert/alert.service';

import UserService from '@/entities/user/user.service';

type ViewPreferenceUpdateComponentType = InstanceType<typeof ViewPreferenceUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const viewPreferenceSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<ViewPreferenceUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('ViewPreference Management Update Component', () => {
    let comp: ViewPreferenceUpdateComponentType;
    let viewPreferenceServiceStub: SinonStubbedInstance<ViewPreferenceService>;

    beforeEach(() => {
      route = {};
      viewPreferenceServiceStub = sinon.createStubInstance<ViewPreferenceService>(ViewPreferenceService);
      viewPreferenceServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          viewPreferenceService: () => viewPreferenceServiceStub,

          userService: () =>
            sinon.createStubInstance<UserService>(UserService, {
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
        const wrapper = shallowMount(ViewPreferenceUpdate, { global: mountOptions });
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
        const wrapper = shallowMount(ViewPreferenceUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.viewPreference = viewPreferenceSample;
        viewPreferenceServiceStub.update.resolves(viewPreferenceSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(viewPreferenceServiceStub.update.calledWith(viewPreferenceSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        viewPreferenceServiceStub.create.resolves(entity);
        const wrapper = shallowMount(ViewPreferenceUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.viewPreference = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(viewPreferenceServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        viewPreferenceServiceStub.find.resolves(viewPreferenceSample);
        viewPreferenceServiceStub.retrieve.resolves([viewPreferenceSample]);

        // WHEN
        route = {
          params: {
            viewPreferenceId: `${viewPreferenceSample.id}`,
          },
        };
        const wrapper = shallowMount(ViewPreferenceUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.viewPreference).toMatchObject(viewPreferenceSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        viewPreferenceServiceStub.find.resolves(viewPreferenceSample);
        const wrapper = shallowMount(ViewPreferenceUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
