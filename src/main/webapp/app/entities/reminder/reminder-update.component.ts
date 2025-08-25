import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import ReminderService from './reminder.service';
import useDataUtils from '@/shared/data/data-utils.service';
import { useDateFormat, useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import ScheduleService from '@/entities/schedule/schedule.service';
import { type ISchedule } from '@/shared/model/schedule.model';
import { type IReminder, Reminder } from '@/shared/model/reminder.model';
import { ReminderChannel } from '@/shared/model/enumerations/reminder-channel.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'ReminderUpdate',
  setup() {
    const reminderService = inject('reminderService', () => new ReminderService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const reminder: Ref<IReminder> = ref(new Reminder());

    const scheduleService = inject('scheduleService', () => new ScheduleService());

    const schedules: Ref<ISchedule[]> = ref([]);
    const reminderChannelValues: Ref<string[]> = ref(Object.keys(ReminderChannel));
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'zh-cn'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveReminder = async reminderId => {
      try {
        const res = await reminderService().find(reminderId);
        res.remindAt = new Date(res.remindAt);
        res.lastAttemptAt = new Date(res.lastAttemptAt);
        reminder.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.reminderId) {
      retrieveReminder(route.params.reminderId);
    }

    const initRelationships = () => {
      scheduleService()
        .retrieve()
        .then(res => {
          schedules.value = res.data;
        });
    };

    initRelationships();

    const dataUtils = useDataUtils();

    const validations = useValidation();
    const validationRules = {
      remindAt: {
        required: validations.required('本字段不能为空.'),
      },
      channel: {
        required: validations.required('本字段不能为空.'),
      },
      subject: {
        maxLength: validations.maxLength('本字段最大长度为 140 个字符.', 140),
      },
      content: {},
      sent: {
        required: validations.required('本字段不能为空.'),
      },
      attemptCount: {},
      lastAttemptAt: {},
      lastErrorMsg: {
        maxLength: validations.maxLength('本字段最大长度为 500 个字符.', 500),
      },
      errorMsg: {
        maxLength: validations.maxLength('本字段最大长度为 500 个字符.', 500),
      },
      schedule: {},
    };
    const v$ = useVuelidate(validationRules, reminder as any);
    v$.value.$validate();

    return {
      reminderService,
      alertService,
      reminder,
      previousState,
      reminderChannelValues,
      isSaving,
      currentLanguage,
      schedules,
      ...dataUtils,
      v$,
      ...useDateFormat({ entityRef: reminder }),
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.reminder.id) {
        this.reminderService()
          .update(this.reminder)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(`A Reminder is updated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.reminderService()
          .create(this.reminder)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(`A Reminder is created with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
