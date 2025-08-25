import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import ViewPreferenceService from './view-preference.service';
import { useDateFormat, useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import UserService from '@/entities/user/user.service';
import { type IViewPreference, ViewPreference } from '@/shared/model/view-preference.model';
import { ViewType } from '@/shared/model/enumerations/view-type.model';
import { WeekStart } from '@/shared/model/enumerations/week-start.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'ViewPreferenceUpdate',
  setup() {
    const viewPreferenceService = inject('viewPreferenceService', () => new ViewPreferenceService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const viewPreference: Ref<IViewPreference> = ref(new ViewPreference());
    const userService = inject('userService', () => new UserService());
    const users: Ref<Array<any>> = ref([]);
    const viewTypeValues: Ref<string[]> = ref(Object.keys(ViewType));
    const weekStartValues: Ref<string[]> = ref(Object.keys(WeekStart));
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'zh-cn'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveViewPreference = async viewPreferenceId => {
      try {
        const res = await viewPreferenceService().find(viewPreferenceId);
        res.lastStart = new Date(res.lastStart);
        res.lastEnd = new Date(res.lastEnd);
        viewPreference.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.viewPreferenceId) {
      retrieveViewPreference(route.params.viewPreferenceId);
    }

    const initRelationships = () => {
      userService()
        .retrieve()
        .then(res => {
          users.value = res.data;
        });
    };

    initRelationships();

    const validations = useValidation();
    const validationRules = {
      defaultView: {
        required: validations.required('本字段不能为空.'),
      },
      lastStart: {},
      lastEnd: {},
      weekStart: {
        required: validations.required('本字段不能为空.'),
      },
      showWeekend: {
        required: validations.required('本字段不能为空.'),
      },
      user: {},
    };
    const v$ = useVuelidate(validationRules, viewPreference as any);
    v$.value.$validate();

    return {
      viewPreferenceService,
      alertService,
      viewPreference,
      previousState,
      viewTypeValues,
      weekStartValues,
      isSaving,
      currentLanguage,
      users,
      v$,
      ...useDateFormat({ entityRef: viewPreference }),
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.viewPreference.id) {
        this.viewPreferenceService()
          .update(this.viewPreference)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(`A ViewPreference is updated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.viewPreferenceService()
          .create(this.viewPreference)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(`A ViewPreference is created with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
