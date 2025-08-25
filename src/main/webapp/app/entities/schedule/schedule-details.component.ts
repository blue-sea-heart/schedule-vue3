import { type Ref, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import ScheduleService from './schedule.service';
import useDataUtils from '@/shared/data/data-utils.service';
import { useDateFormat } from '@/shared/composables';
import { type ISchedule } from '@/shared/model/schedule.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'ScheduleDetails',
  setup() {
    const dateFormat = useDateFormat();
    const scheduleService = inject('scheduleService', () => new ScheduleService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const dataUtils = useDataUtils();

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const schedule: Ref<ISchedule> = ref({});

    const retrieveSchedule = async scheduleId => {
      try {
        const res = await scheduleService().find(scheduleId);
        schedule.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.scheduleId) {
      retrieveSchedule(route.params.scheduleId);
    }

    return {
      ...dateFormat,
      alertService,
      schedule,

      ...dataUtils,

      previousState,
    };
  },
});
