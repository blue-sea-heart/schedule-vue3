import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import dayjs from 'dayjs';
import InAppNotificationUpdate from './in-app-notification-update.vue';
import InAppNotificationService from './in-app-notification.service';
import { DATE_TIME_LONG_FORMAT } from '@/shared/composables/date-format';
import AlertService from '@/shared/alert/alert.service';

import UserService from '@/entities/user/user.service';

type InAppNotificationUpdateComponentType = InstanceType<typeof InAppNotificationUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const inAppNotificationSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<InAppNotificationUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('InAppNotification Management Update Component', () => {
    let comp: InAppNotificationUpdateComponentType;
    let inAppNotificationServiceStub: SinonStubbedInstance<InAppNotificationService>;

    beforeEach(() => {
      route = {};
      inAppNotificationServiceStub = sinon.createStubInstance<InAppNotificationService>(InAppNotificationService);
      inAppNotificationServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          inAppNotificationService: () => inAppNotificationServiceStub,

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
        const wrapper = shallowMount(InAppNotificationUpdate, { global: mountOptions });
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
        const wrapper = shallowMount(InAppNotificationUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.inAppNotification = inAppNotificationSample;
        inAppNotificationServiceStub.update.resolves(inAppNotificationSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(inAppNotificationServiceStub.update.calledWith(inAppNotificationSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        inAppNotificationServiceStub.create.resolves(entity);
        const wrapper = shallowMount(InAppNotificationUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.inAppNotification = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(inAppNotificationServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        inAppNotificationServiceStub.find.resolves(inAppNotificationSample);
        inAppNotificationServiceStub.retrieve.resolves([inAppNotificationSample]);

        // WHEN
        route = {
          params: {
            inAppNotificationId: `${inAppNotificationSample.id}`,
          },
        };
        const wrapper = shallowMount(InAppNotificationUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.inAppNotification).toMatchObject(inAppNotificationSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        inAppNotificationServiceStub.find.resolves(inAppNotificationSample);
        const wrapper = shallowMount(InAppNotificationUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
