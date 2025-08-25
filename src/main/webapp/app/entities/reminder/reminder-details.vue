<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="reminder">
        <h2 class="jh-entity-heading" data-cy="reminderDetailsHeading"><span>Reminder</span> {{ reminder.id }}</h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Remind At</span>
          </dt>
          <dd>
            <span v-if="reminder.remindAt">{{ formatDateLong(reminder.remindAt) }}</span>
          </dd>
          <dt>
            <span>Channel</span>
          </dt>
          <dd>
            <span>{{ reminder.channel }}</span>
          </dd>
          <dt>
            <span>Subject</span>
          </dt>
          <dd>
            <span>{{ reminder.subject }}</span>
          </dd>
          <dt>
            <span>Content</span>
          </dt>
          <dd>
            <span>{{ reminder.content }}</span>
          </dd>
          <dt>
            <span>Sent</span>
          </dt>
          <dd>
            <span>{{ reminder.sent }}</span>
          </dd>
          <dt>
            <span>Attempt Count</span>
          </dt>
          <dd>
            <span>{{ reminder.attemptCount }}</span>
          </dd>
          <dt>
            <span>Last Attempt At</span>
          </dt>
          <dd>
            <span v-if="reminder.lastAttemptAt">{{ formatDateLong(reminder.lastAttemptAt) }}</span>
          </dd>
          <dt>
            <span>Last Error Msg</span>
          </dt>
          <dd>
            <span>{{ reminder.lastErrorMsg }}</span>
          </dd>
          <dt>
            <span>Error Msg</span>
          </dt>
          <dd>
            <span>{{ reminder.errorMsg }}</span>
          </dd>
          <dt>
            <span>Schedule</span>
          </dt>
          <dd>
            <div v-if="reminder.schedule">
              <router-link :to="{ name: 'ScheduleView', params: { scheduleId: reminder.schedule.id } }">{{
                reminder.schedule.id
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>返回</span>
        </button>
        <router-link v-if="reminder.id" :to="{ name: 'ReminderEdit', params: { reminderId: reminder.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span>编辑</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./reminder-details.component.ts"></script>
