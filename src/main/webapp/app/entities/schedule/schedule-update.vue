<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="scheduleVue3App.schedule.home.createOrEditLabel" data-cy="ScheduleCreateUpdateHeading">创建或编辑 Schedule</h2>
        <div>
          <div class="form-group" v-if="schedule.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="schedule.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="schedule-title">Title</label>
            <input
              type="text"
              class="form-control"
              name="title"
              id="schedule-title"
              data-cy="title"
              :class="{ valid: !v$.title.$invalid, invalid: v$.title.$invalid }"
              v-model="v$.title.$model"
              required
            />
            <div v-if="v$.title.$anyDirty && v$.title.$invalid">
              <small class="form-text text-danger" v-for="error of v$.title.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="schedule-description">Description</label>
            <textarea
              class="form-control"
              name="description"
              id="schedule-description"
              data-cy="description"
              :class="{ valid: !v$.description.$invalid, invalid: v$.description.$invalid }"
              v-model="v$.description.$model"
            ></textarea>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="schedule-location">Location</label>
            <input
              type="text"
              class="form-control"
              name="location"
              id="schedule-location"
              data-cy="location"
              :class="{ valid: !v$.location.$invalid, invalid: v$.location.$invalid }"
              v-model="v$.location.$model"
            />
            <div v-if="v$.location.$anyDirty && v$.location.$invalid">
              <small class="form-text text-danger" v-for="error of v$.location.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="schedule-allDay">All Day</label>
            <input
              type="checkbox"
              class="form-check"
              name="allDay"
              id="schedule-allDay"
              data-cy="allDay"
              :class="{ valid: !v$.allDay.$invalid, invalid: v$.allDay.$invalid }"
              v-model="v$.allDay.$model"
              required
            />
            <div v-if="v$.allDay.$anyDirty && v$.allDay.$invalid">
              <small class="form-text text-danger" v-for="error of v$.allDay.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="schedule-startTime">Start Time</label>
            <div class="d-flex">
              <input
                id="schedule-startTime"
                data-cy="startTime"
                type="datetime-local"
                class="form-control"
                name="startTime"
                :class="{ valid: !v$.startTime.$invalid, invalid: v$.startTime.$invalid }"
                required
                :value="convertDateTimeFromServer(v$.startTime.$model)"
                @change="updateZonedDateTimeField('startTime', $event)"
              />
            </div>
            <div v-if="v$.startTime.$anyDirty && v$.startTime.$invalid">
              <small class="form-text text-danger" v-for="error of v$.startTime.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="schedule-endTime">End Time</label>
            <div class="d-flex">
              <input
                id="schedule-endTime"
                data-cy="endTime"
                type="datetime-local"
                class="form-control"
                name="endTime"
                :class="{ valid: !v$.endTime.$invalid, invalid: v$.endTime.$invalid }"
                :value="convertDateTimeFromServer(v$.endTime.$model)"
                @change="updateZonedDateTimeField('endTime', $event)"
              />
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="schedule-priority">Priority</label>
            <select
              class="form-control"
              name="priority"
              :class="{ valid: !v$.priority.$invalid, invalid: v$.priority.$invalid }"
              v-model="v$.priority.$model"
              id="schedule-priority"
              data-cy="priority"
              required
            >
              <option v-for="priority in priorityValues" :key="priority" :value="priority">{{ priority }}</option>
            </select>
            <div v-if="v$.priority.$anyDirty && v$.priority.$invalid">
              <small class="form-text text-danger" v-for="error of v$.priority.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="schedule-completedAt">Completed At</label>
            <div class="d-flex">
              <input
                id="schedule-completedAt"
                data-cy="completedAt"
                type="datetime-local"
                class="form-control"
                name="completedAt"
                :class="{ valid: !v$.completedAt.$invalid, invalid: v$.completedAt.$invalid }"
                :value="convertDateTimeFromServer(v$.completedAt.$model)"
                @change="updateZonedDateTimeField('completedAt', $event)"
              />
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="schedule-visibility">Visibility</label>
            <select
              class="form-control"
              name="visibility"
              :class="{ valid: !v$.visibility.$invalid, invalid: v$.visibility.$invalid }"
              v-model="v$.visibility.$model"
              id="schedule-visibility"
              data-cy="visibility"
              required
            >
              <option v-for="visibility in visibilityValues" :key="visibility" :value="visibility">{{ visibility }}</option>
            </select>
            <div v-if="v$.visibility.$anyDirty && v$.visibility.$invalid">
              <small class="form-text text-danger" v-for="error of v$.visibility.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="schedule-owner">Owner</label>
            <select class="form-control" id="schedule-owner" data-cy="owner" name="owner" v-model="schedule.owner">
              <option :value="null"></option>
              <option
                :value="schedule.owner && userOption.id === schedule.owner.id ? schedule.owner : userOption"
                v-for="userOption in users"
                :key="userOption.id"
              >
                {{ userOption.login }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="schedule-status">Status</label>
            <select class="form-control" id="schedule-status" data-cy="status" name="status" v-model="schedule.status">
              <option :value="null"></option>
              <option
                :value="schedule.status && scheduleStatusOption.id === schedule.status.id ? schedule.status : scheduleStatusOption"
                v-for="scheduleStatusOption in scheduleStatuses"
                :key="scheduleStatusOption.id"
              >
                {{ scheduleStatusOption.name }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="schedule-category">Category</label>
            <select class="form-control" id="schedule-category" data-cy="category" name="category" v-model="schedule.category">
              <option :value="null"></option>
              <option
                :value="schedule.category && categoryOption.id === schedule.category.id ? schedule.category : categoryOption"
                v-for="categoryOption in categories"
                :key="categoryOption.id"
              >
                {{ categoryOption.name }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label for="schedule-tags">Tags</label>
            <select
              class="form-control"
              id="schedule-tags"
              data-cy="tags"
              multiple
              name="tags"
              v-if="schedule.tags !== undefined"
              v-model="schedule.tags"
            >
              <option :value="getSelected(schedule.tags, tagOption, 'id')" v-for="tagOption in tags" :key="tagOption.id">
                {{ tagOption.name }}
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
<script lang="ts" src="./schedule-update.component.ts"></script>
