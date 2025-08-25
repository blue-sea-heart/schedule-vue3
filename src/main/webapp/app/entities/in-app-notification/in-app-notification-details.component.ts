import { type Ref, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import InAppNotificationService from './in-app-notification.service';
import useDataUtils from '@/shared/data/data-utils.service';
import { useDateFormat } from '@/shared/composables';
import { type IInAppNotification } from '@/shared/model/in-app-notification.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'InAppNotificationDetails',
  setup() {
    const dateFormat = useDateFormat();
    const inAppNotificationService = inject('inAppNotificationService', () => new InAppNotificationService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const dataUtils = useDataUtils();

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const inAppNotification: Ref<IInAppNotification> = ref({});

    const retrieveInAppNotification = async inAppNotificationId => {
      try {
        const res = await inAppNotificationService().find(inAppNotificationId);
        inAppNotification.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.inAppNotificationId) {
      retrieveInAppNotification(route.params.inAppNotificationId);
    }

    return {
      ...dateFormat,
      alertService,
      inAppNotification,

      ...dataUtils,

      previousState,
    };
  },
});
