<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="scheduleVue3App.reminder.home.createOrEditLabel" data-cy="ReminderCreateUpdateHeading">创建或编辑 Reminder</h2>
        <div>
          <div class="form-group" v-if="reminder.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="reminder.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="reminder-remindAt">Remind At</label>
            <div class="d-flex">
              <input
                id="reminder-remindAt"
                data-cy="remindAt"
                type="datetime-local"
                class="form-control"
                name="remindAt"
                :class="{ valid: !v$.remindAt.$invalid, invalid: v$.remindAt.$invalid }"
                required
                :value="convertDateTimeFromServer(v$.remindAt.$model)"
                @change="updateZonedDateTimeField('remindAt', $event)"
              />
            </div>
            <div v-if="v$.remindAt.$anyDirty && v$.remindAt.$invalid">
              <small class="form-text text-danger" v-for="error of v$.remindAt.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="reminder-channel">Channel</label>
            <select
              class="form-control"
              name="channel"
              :class="{ valid: !v$.channel.$invalid, invalid: v$.channel.$invalid }"
              v-model="v$.channel.$model"
              id="reminder-channel"
              data-cy="channel"
              required
            >
              <option v-for="reminderChannel in reminderChannelValues" :key="reminderChannel" :value="reminderChannel">
                {{ reminderChannel }}
              </option>
            </select>
            <div v-if="v$.channel.$anyDirty && v$.channel.$invalid">
              <small class="form-text text-danger" v-for="error of v$.channel.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="reminder-subject">Subject</label>
            <input
              type="text"
              class="form-control"
              name="subject"
              id="reminder-subject"
              data-cy="subject"
              :class="{ valid: !v$.subject.$invalid, invalid: v$.subject.$invalid }"
              v-model="v$.subject.$model"
            />
            <div v-if="v$.subject.$anyDirty && v$.subject.$invalid">
              <small class="form-text text-danger" v-for="error of v$.subject.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="reminder-content">Content</label>
            <textarea
              class="form-control"
              name="content"
              id="reminder-content"
              data-cy="content"
              :class="{ valid: !v$.content.$invalid, invalid: v$.content.$invalid }"
              v-model="v$.content.$model"
            ></textarea>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="reminder-sent">Sent</label>
            <input
              type="checkbox"
              class="form-check"
              name="sent"
              id="reminder-sent"
              data-cy="sent"
              :class="{ valid: !v$.sent.$invalid, invalid: v$.sent.$invalid }"
              v-model="v$.sent.$model"
              required
            />
            <div v-if="v$.sent.$anyDirty && v$.sent.$invalid">
              <small class="form-text text-danger" v-for="error of v$.sent.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="reminder-attemptCount">Attempt Count</label>
            <input
              type="number"
              class="form-control"
              name="attemptCount"
              id="reminder-attemptCount"
              data-cy="attemptCount"
              :class="{ valid: !v$.attemptCount.$invalid, invalid: v$.attemptCount.$invalid }"
              v-model.number="v$.attemptCount.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="reminder-lastAttemptAt">Last Attempt At</label>
            <div class="d-flex">
              <input
                id="reminder-lastAttemptAt"
                data-cy="lastAttemptAt"
                type="datetime-local"
                class="form-control"
                name="lastAttemptAt"
                :class="{ valid: !v$.lastAttemptAt.$invalid, invalid: v$.lastAttemptAt.$invalid }"
                :value="convertDateTimeFromServer(v$.lastAttemptAt.$model)"
                @change="updateZonedDateTimeField('lastAttemptAt', $event)"
              />
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="reminder-lastErrorMsg">Last Error Msg</label>
            <input
              type="text"
              class="form-control"
              name="lastErrorMsg"
              id="reminder-lastErrorMsg"
              data-cy="lastErrorMsg"
              :class="{ valid: !v$.lastErrorMsg.$invalid, invalid: v$.lastErrorMsg.$invalid }"
              v-model="v$.lastErrorMsg.$model"
            />
            <div v-if="v$.lastErrorMsg.$anyDirty && v$.lastErrorMsg.$invalid">
              <small class="form-text text-danger" v-for="error of v$.lastErrorMsg.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="reminder-errorMsg">Error Msg</label>
            <input
              type="text"
              class="form-control"
              name="errorMsg"
              id="reminder-errorMsg"
              data-cy="errorMsg"
              :class="{ valid: !v$.errorMsg.$invalid, invalid: v$.errorMsg.$invalid }"
              v-model="v$.errorMsg.$model"
            />
            <div v-if="v$.errorMsg.$anyDirty && v$.errorMsg.$invalid">
              <small class="form-text text-danger" v-for="error of v$.errorMsg.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="reminder-schedule">Schedule</label>
            <select class="form-control" id="reminder-schedule" data-cy="schedule" name="schedule" v-model="reminder.schedule">
              <option :value="null"></option>
              <option
                :value="reminder.schedule && scheduleOption.id === reminder.schedule.id ? reminder.schedule : scheduleOption"
                v-for="scheduleOption in schedules"
                :key="scheduleOption.id"
              >
                {{ scheduleOption.id }}
              </option>
            </select>
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" @click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span>取消</span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="v$.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>保存</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./reminder-update.component.ts"></script>
