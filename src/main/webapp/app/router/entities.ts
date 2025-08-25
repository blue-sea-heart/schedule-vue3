import { Authority } from '@/shared/security/authority';
const Entities = () => import('@/entities/entities.vue');

const ScheduleStatus = () => import('@/entities/schedule-status/schedule-status.vue');
const ScheduleStatusUpdate = () => import('@/entities/schedule-status/schedule-status-update.vue');
const ScheduleStatusDetails = () => import('@/entities/schedule-status/schedule-status-details.vue');

const Category = () => import('@/entities/category/category.vue');
const CategoryUpdate = () => import('@/entities/category/category-update.vue');
const CategoryDetails = () => import('@/entities/category/category-details.vue');

const Tag = () => import('@/entities/tag/tag.vue');
const TagUpdate = () => import('@/entities/tag/tag-update.vue');
const TagDetails = () => import('@/entities/tag/tag-details.vue');

const Schedule = () => import('@/entities/schedule/schedule.vue');
const ScheduleUpdate = () => import('@/entities/schedule/schedule-update.vue');
const ScheduleDetails = () => import('@/entities/schedule/schedule-details.vue');

const Reminder = () => import('@/entities/reminder/reminder.vue');
const ReminderUpdate = () => import('@/entities/reminder/reminder-update.vue');
const ReminderDetails = () => import('@/entities/reminder/reminder-details.vue');

const InAppNotification = () => import('@/entities/in-app-notification/in-app-notification.vue');
const InAppNotificationUpdate = () => import('@/entities/in-app-notification/in-app-notification-update.vue');
const InAppNotificationDetails = () => import('@/entities/in-app-notification/in-app-notification-details.vue');

const ViewPreference = () => import('@/entities/view-preference/view-preference.vue');
const ViewPreferenceUpdate = () => import('@/entities/view-preference/view-preference-update.vue');
const ViewPreferenceDetails = () => import('@/entities/view-preference/view-preference-details.vue');

// jhipster-needle-add-entity-to-router-import - JHipster will import entities to the router here

export default {
  path: '/',
  component: Entities,
  children: [
    {
      path: 'schedule-status',
      name: 'ScheduleStatus',
      component: ScheduleStatus,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'schedule-status/new',
      name: 'ScheduleStatusCreate',
      component: ScheduleStatusUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'schedule-status/:scheduleStatusId/edit',
      name: 'ScheduleStatusEdit',
      component: ScheduleStatusUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'schedule-status/:scheduleStatusId/view',
      name: 'ScheduleStatusView',
      component: ScheduleStatusDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'category',
      name: 'Category',
      component: Category,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'category/new',
      name: 'CategoryCreate',
      component: CategoryUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'category/:categoryId/edit',
      name: 'CategoryEdit',
      component: CategoryUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'category/:categoryId/view',
      name: 'CategoryView',
      component: CategoryDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'tag',
      name: 'Tag',
      component: Tag,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'tag/new',
      name: 'TagCreate',
      component: TagUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'tag/:tagId/edit',
      name: 'TagEdit',
      component: TagUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'tag/:tagId/view',
      name: 'TagView',
      component: TagDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'schedule',
      name: 'Schedule',
      component: Schedule,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'schedule/new',
      name: 'ScheduleCreate',
      component: ScheduleUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'schedule/:scheduleId/edit',
      name: 'ScheduleEdit',
      component: ScheduleUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'schedule/:scheduleId/view',
      name: 'ScheduleView',
      component: ScheduleDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'reminder',
      name: 'Reminder',
      component: Reminder,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'reminder/new',
      name: 'ReminderCreate',
      component: ReminderUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'reminder/:reminderId/edit',
      name: 'ReminderEdit',
      component: ReminderUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'reminder/:reminderId/view',
      name: 'ReminderView',
      component: ReminderDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'in-app-notification',
      name: 'InAppNotification',
      component: InAppNotification,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'in-app-notification/new',
      name: 'InAppNotificationCreate',
      component: InAppNotificationUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'in-app-notification/:inAppNotificationId/edit',
      name: 'InAppNotificationEdit',
      component: InAppNotificationUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'in-app-notification/:inAppNotificationId/view',
      name: 'InAppNotificationView',
      component: InAppNotificationDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'view-preference',
      name: 'ViewPreference',
      component: ViewPreference,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'view-preference/new',
      name: 'ViewPreferenceCreate',
      component: ViewPreferenceUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'view-preference/:viewPreferenceId/edit',
      name: 'ViewPreferenceEdit',
      component: ViewPreferenceUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'view-preference/:viewPreferenceId/view',
      name: 'ViewPreferenceView',
      component: ViewPreferenceDetails,
      meta: { authorities: [Authority.USER] },
    },
    // jhipster-needle-add-entity-to-router - JHipster will add entities to the router here
  ],
};
