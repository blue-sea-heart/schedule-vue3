import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import ScheduleStatusService from './schedule-status.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import { type IScheduleStatus, ScheduleStatus } from '@/shared/model/schedule-status.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'ScheduleStatusUpdate',
  setup() {
    const scheduleStatusService = inject('scheduleStatusService', () => new ScheduleStatusService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const scheduleStatus: Ref<IScheduleStatus> = ref(new ScheduleStatus());
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'zh-cn'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

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

    const validations = useValidation();
    const validationRules = {
      code: {
        required: validations.required('本字段不能为空.'),
        maxLength: validations.maxLength('本字段最大长度为 40 个字符.', 40),
      },
      name: {
        required: validations.required('本字段不能为空.'),
        maxLength: validations.maxLength('本字段最大长度为 60 个字符.', 60),
      },
      color: {
        maxLength: validations.maxLength('本字段最大长度为 20 个字符.', 20),
      },
      sortNo: {
        required: validations.required('本字段不能为空.'),
        integer: validations.integer('本字段必须为数字.'),
      },
      isDefault: {
        required: validations.required('本字段不能为空.'),
      },
      isTerminal: {
        required: validations.required('本字段不能为空.'),
      },
    };
    const v$ = useVuelidate(validationRules, scheduleStatus as any);
    v$.value.$validate();

    return {
      scheduleStatusService,
      alertService,
      scheduleStatus,
      previousState,
      isSaving,
      currentLanguage,
      v$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.scheduleStatus.id) {
        this.scheduleStatusService()
          .update(this.scheduleStatus)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(`A ScheduleStatus is updated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.scheduleStatusService()
          .create(this.scheduleStatus)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(`A ScheduleStatus is created with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
