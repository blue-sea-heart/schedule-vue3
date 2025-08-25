import { defineComponent, provide } from 'vue';

import ScheduleStatusService from './schedule-status/schedule-status.service';
import CategoryService from './category/category.service';
import TagService from './tag/tag.service';
import ScheduleService from './schedule/schedule.service';
import ReminderService from './reminder/reminder.service';
import InAppNotificationService from './in-app-notification/in-app-notification.service';
import ViewPreferenceService from './view-preference/view-preference.service';
import UserService from '@/entities/user/user.service';
// jhipster-needle-add-entity-service-to-entities-component-import - JHipster will import entities services here

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'Entities',
  setup() {
    provide('userService', () => new UserService());
    provide('scheduleStatusService', () => new ScheduleStatusService());
    provide('categoryService', () => new CategoryService());
    provide('tagService', () => new TagService());
    provide('scheduleService', () => new ScheduleService());
    provide('reminderService', () => new ReminderService());
    provide('inAppNotificationService', () => new InAppNotificationService());
    provide('viewPreferenceService', () => new ViewPreferenceService());
    // jhipster-needle-add-entity-service-to-entities-component - JHipster will import entities services here
  },
});
