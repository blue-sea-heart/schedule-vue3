import { type Ref, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import ScheduleStatusService from './schedule-status.service';
import { type IScheduleStatus } from '@/shared/model/schedule-status.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'ScheduleStatusDetails',
  setup() {
    const scheduleStatusService = inject('scheduleStatusService', () => new ScheduleStatusService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const scheduleStatus: Ref<IScheduleStatus> = ref({});

    const retrieveScheduleStatus = async scheduleStatusId => {
      try {
        const res = await scheduleStatusService().find(scheduleStatusId);
        scheduleStatus.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.scheduleStatusId) {
      retrieveScheduleStatus(route.params.scheduleStatusId);
    }

    return {
      alertService,
      scheduleStatus,

      previousState,
    };
  },
});
