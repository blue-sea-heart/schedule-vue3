import { type Ref, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import ViewPreferenceService from './view-preference.service';
import { useDateFormat } from '@/shared/composables';
import { type IViewPreference } from '@/shared/model/view-preference.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'ViewPreferenceDetails',
  setup() {
    const dateFormat = useDateFormat();
    const viewPreferenceService = inject('viewPreferenceService', () => new ViewPreferenceService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const viewPreference: Ref<IViewPreference> = ref({});

    const retrieveViewPreference = async viewPreferenceId => {
      try {
        const res = await viewPreferenceService().find(viewPreferenceId);
        viewPreference.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.viewPreferenceId) {
      retrieveViewPreference(route.params.viewPreferenceId);
    }

    return {
      ...dateFormat,
      alertService,
      viewPreference,

      previousState,
    };
  },
});
