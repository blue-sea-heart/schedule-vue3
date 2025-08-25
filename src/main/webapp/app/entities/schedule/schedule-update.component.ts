import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import ScheduleService from './schedule.service';
import useDataUtils from '@/shared/data/data-utils.service';
import { useDateFormat, useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import UserService from '@/entities/user/user.service';
import ScheduleStatusService from '@/entities/schedule-status/schedule-status.service';
import { type IScheduleStatus } from '@/shared/model/schedule-status.model';
import CategoryService from '@/entities/category/category.service';
import { type ICategory } from '@/shared/model/category.model';
import TagService from '@/entities/tag/tag.service';
import { type ITag } from '@/shared/model/tag.model';
import { type ISchedule, Schedule } from '@/shared/model/schedule.model';
import { Priority } from '@/shared/model/enumerations/priority.model';
import { Visibility } from '@/shared/model/enumerations/visibility.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'ScheduleUpdate',
  setup() {
    const scheduleService = inject('scheduleService', () => new ScheduleService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const schedule: Ref<ISchedule> = ref(new Schedule());
    const userService = inject('userService', () => new UserService());
    const users: Ref<Array<any>> = ref([]);

    const scheduleStatusService = inject('scheduleStatusService', () => new ScheduleStatusService());

    const scheduleStatuses: Ref<IScheduleStatus[]> = ref([]);

    const categoryService = inject('categoryService', () => new CategoryService());

    const categories: Ref<ICategory[]> = ref([]);

    const tagService = inject('tagService', () => new TagService());

    const tags: Ref<ITag[]> = ref([]);
    const priorityValues: Ref<string[]> = ref(Object.keys(Priority));
    const visibilityValues: Ref<string[]> = ref(Object.keys(Visibility));
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'zh-cn'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveSchedule = async scheduleId => {
      try {
        const res = await scheduleService().find(scheduleId);
        res.startTime = new Date(res.startTime);
        res.endTime = new Date(res.endTime);
        res.completedAt = new Date(res.completedAt);
        schedule.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.scheduleId) {
      retrieveSchedule(route.params.scheduleId);
    }

    const initRelationships = () => {
      userService()
        .retrieve()
        .then(res => {
          users.value = res.data;
        });
      scheduleStatusService()
        .retrieve()
        .then(res => {
          scheduleStatuses.value = res.data;
        });
      categoryService()
        .retrieve()
        .then(res => {
          categories.value = res.data;
        });
      tagService()
        .retrieve()
        .then(res => {
          tags.value = res.data;
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
      description: {},
      location: {
        maxLength: validations.maxLength('本字段最大长度为 140 个字符.', 140),
      },
      allDay: {
        required: validations.required('本字段不能为空.'),
      },
      startTime: {
        required: validations.required('本字段不能为空.'),
      },
      endTime: {},
      priority: {
        required: validations.required('本字段不能为空.'),
      },
      completedAt: {},
      visibility: {
        required: validations.required('本字段不能为空.'),
      },
      reminders: {},
      owner: {},
      status: {},
      category: {},
      tags: {},
    };
    const v$ = useVuelidate(validationRules, schedule as any);
    v$.value.$validate();

    return {
      scheduleService,
      alertService,
      schedule,
      previousState,
      priorityValues,
      visibilityValues,
      isSaving,
      currentLanguage,
      users,
      scheduleStatuses,
      categories,
      tags,
      ...dataUtils,
      v$,
      ...useDateFormat({ entityRef: schedule }),
    };
  },
  created(): void {
    this.schedule.tags = [];
  },
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.schedule.id) {
        this.scheduleService()
          .update(this.schedule)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(`A Schedule is updated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.scheduleService()
          .create(this.schedule)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(`A Schedule is created with identifier ${param.id}`);
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
