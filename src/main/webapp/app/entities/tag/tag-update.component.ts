import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import TagService from './tag.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import ScheduleService from '@/entities/schedule/schedule.service';
import { type ISchedule } from '@/shared/model/schedule.model';
import { type ITag, Tag } from '@/shared/model/tag.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'TagUpdate',
  setup() {
    const tagService = inject('tagService', () => new TagService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const tag: Ref<ITag> = ref(new Tag());

    const scheduleService = inject('scheduleService', () => new ScheduleService());

    const schedules: Ref<ISchedule[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'zh-cn'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveTag = async tagId => {
      try {
        const res = await tagService().find(tagId);
        tag.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.tagId) {
      retrieveTag(route.params.tagId);
    }

    const initRelationships = () => {
      scheduleService()
        .retrieve()
        .then(res => {
          schedules.value = res.data;
        });
    };

    initRelationships();

    const validations = useValidation();
    const validationRules = {
      name: {
        required: validations.required('本字段不能为空.'),
        maxLength: validations.maxLength('本字段最大长度为 60 个字符.', 60),
      },
      color: {
        maxLength: validations.maxLength('本字段最大长度为 20 个字符.', 20),
      },
      schedules: {},
    };
    const v$ = useVuelidate(validationRules, tag as any);
    v$.value.$validate();

    return {
      tagService,
      alertService,
      tag,
      previousState,
      isSaving,
      currentLanguage,
      schedules,
      v$,
    };
  },
  created(): void {
    this.tag.schedules = [];
  },
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.tag.id) {
        this.tagService()
          .update(this.tag)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(`A Tag is updated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.tagService()
          .create(this.tag)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(`A Tag is created with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },

    getSelected(selectedVals, option, pkField = 'id'): any {
      if (selectedVals) {
        return selectedVals.find(value => option[pkField] === value[pkField]) ?? option;
      }
      return option;
    },
  },
});
