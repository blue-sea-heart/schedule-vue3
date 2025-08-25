import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import ViewPreferenceDetails from './view-preference-details.vue';
import ViewPreferenceService from './view-preference.service';
import AlertService from '@/shared/alert/alert.service';

type ViewPreferenceDetailsComponentType = InstanceType<typeof ViewPreferenceDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const viewPreferenceSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('ViewPreference Management Detail Component', () => {
    let viewPreferenceServiceStub: SinonStubbedInstance<ViewPreferenceService>;
    let mountOptions: MountingOptions<ViewPreferenceDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      viewPreferenceServiceStub = sinon.createStubInstance<ViewPreferenceService>(ViewPreferenceService);

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
          viewPreferenceService: () => viewPreferenceServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        viewPreferenceServiceStub.find.resolves(viewPreferenceSample);
        route = {
          params: {
            viewPreferenceId: `${123}`,
          },
        };
        const wrapper = shallowMount(ViewPreferenceDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.viewPreference).toMatchObject(viewPreferenceSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        viewPreferenceServiceStub.find.resolves(viewPreferenceSample);
        const wrapper = shallowMount(ViewPreferenceDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
