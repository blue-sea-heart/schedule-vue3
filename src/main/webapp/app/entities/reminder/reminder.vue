<template>
  <div>
    <h2 id="page-heading" data-cy="ReminderHeading">
      <span id="reminder-heading">Reminders</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refresh list</span>
        </button>
        <router-link :to="{ name: 'ReminderCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-reminder"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>创建新 Reminder</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && reminders && reminders.length === 0">
      <span>No Reminders found</span>
    </div>
    <div class="table-responsive" v-if="reminders && reminders.length > 0">
      <table class="table table-striped" aria-describedby="reminders">
        <thead>
          <tr>
            <th scope="row" @click="changeOrder('id')">
              <span>ID</span> <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('remindAt')">
              <span>Remind At</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'remindAt'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('channel')">
              <span>Channel</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'channel'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('subject')">
              <span>Subject</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'subject'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('content')">
              <span>Content</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'content'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('sent')">
              <span>Sent</span> <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'sent'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('attemptCount')">
              <span>Attempt Count</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'attemptCount'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('lastAttemptAt')">
              <span>Last Attempt At</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'lastAttemptAt'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('lastErrorMsg')">
              <span>Last Error Msg</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'lastErrorMsg'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('errorMsg')">
              <span>Error Msg</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'errorMsg'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('schedule.id')">
              <span>Schedule</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'schedule.id'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="reminder in reminders" :key="reminder.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'ReminderView', params: { reminderId: reminder.id } }">{{ reminder.id }}</router-link>
            </td>
            <td>{{ formatDateShort(reminder.remindAt) || '' }}</td>
            <td>{{ reminder.channel }}</td>
            <td>{{ reminder.subject }}</td>
            <td>{{ reminder.content }}</td>
            <td>{{ reminder.sent }}</td>
            <td>{{ reminder.attemptCount }}</td>
            <td>{{ formatDateShort(reminder.lastAttemptAt) || '' }}</td>
            <td>{{ reminder.lastErrorMsg }}</td>
            <td>{{ reminder.errorMsg }}</td>
            <td>
              <div v-if="reminder.schedule">
                <router-link :to="{ name: 'ScheduleView', params: { scheduleId: reminder.schedule.id } }">{{
                  reminder.schedule.id
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'ReminderView', params: { reminderId: reminder.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">查看</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'ReminderEdit', params: { reminderId: reminder.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">编辑</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(reminder)"
                  variant="danger"
                  class="btn btn-sm"
                  data-cy="entityDeleteButton"
                  v-b-modal.removeEntity
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                  <span class="d-none d-md-inline">删除</span>
                </b-button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <template #modal-title>
        <span id="scheduleVue3App.reminder.delete.question" data-cy="reminderDeleteDialogHeading">确认删除</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-reminder-heading">你确定要删除 Reminder {{ removeId }} 吗？</p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">取消</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-reminder"
            data-cy="entityConfirmDeleteButton"
            @click="removeReminder()"
          >
            删除
          </button>
        </div>
      </template>
    </b-modal>
    <div v-show="reminders && reminders.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./reminder.component.ts"></script>
