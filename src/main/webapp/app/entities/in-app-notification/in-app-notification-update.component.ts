import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import InAppNotificationService from './in-app-notification.service';
import useDataUtils from '@/shared/data/data-utils.service';
import { useDateFormat, useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import UserService from '@/entities/user/user.service';
import { type IInAppNotification, InAppNotification } from '@/shared/model/in-app-notification.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'InAppNotificationUpdate',
  setup() {
    const inAppNotificationService = inject('inAppNotificationService', () => new InAppNotificationService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const inAppNotification: Ref<IInAppNotification> = ref(new InAppNotification());
    const userService = inject('userService', () => new UserService());
    const users: Ref<Array<any>> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'zh-cn'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveInAppNotification = async inAppNotificationId => {
      try {
        const res = await inAppNotificationService().find(inAppNotificationId);
        res.createdAt = new Date(res.createdAt);
        inAppNotification.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.inAppNotificationId) {
      retrieveInAppNotification(route.params.inAppNotificationId);
    }

    const initRelationships = () => {
      userService()
        .retrieve()
        .then(res => {
          users.value = res.data;
        });
    };

    initRelationships();

    const dataUtils = useDataUtils();

    const validations = useValidation();
    const validationRules = {
      title: {
        required: validations.required('本字段不能为空.'),
        maxLength: validations.maxLength('本字段最大长度为 140 个字符.', 140),
      },
      content: {},
      createdAt: {
        required: validations.required('本字段不能为空.'),
      },
      read: {
        required: validations.required('本字段不能为空.'),
      },
      user: {},
    };
    const v$ = useVuelidate(validationRules, inAppNotification as any);
    v$.value.$validate();

    return {
      inAppNotificationService,
      alertService,
      inAppNotification,
      previousState,
      isSaving,
      currentLanguage,
      users,
      ...dataUtils,
      v$,
      ...useDateFormat({ entityRef: inAppNotification }),
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.inAppNotification.id) {
        this.inAppNotificationService()
          .update(this.inAppNotification)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(`A InAppNotification is updated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.inAppNotificationService()
          .create(this.inAppNotification)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(`A InAppNotification is created with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
