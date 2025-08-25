<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="scheduleVue3App.inAppNotification.home.createOrEditLabel" data-cy="InAppNotificationCreateUpdateHeading">
          创建或编辑 In App Notification
        </h2>
        <div>
          <div class="form-group" v-if="inAppNotification.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="inAppNotification.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="in-app-notification-title">Title</label>
            <input
              type="text"
              class="form-control"
              name="title"
              id="in-app-notification-title"
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
            <label class="form-control-label" for="in-app-notification-content">Content</label>
            <textarea
              class="form-control"
              name="content"
              id="in-app-notification-content"
              data-cy="content"
              :class="{ valid: !v$.content.$invalid, invalid: v$.content.$invalid }"
              v-model="v$.content.$model"
            ></textarea>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="in-app-notification-createdAt">Created At</label>
            <div class="d-flex">
              <input
                id="in-app-notification-createdAt"
                data-cy="createdAt"
                type="datetime-local"
                class="form-control"
                name="createdAt"
                :class="{ valid: !v$.createdAt.$invalid, invalid: v$.createdAt.$invalid }"
                required
                :value="convertDateTimeFromServer(v$.createdAt.$model)"
                @change="updateZonedDateTimeField('createdAt', $event)"
              />
            </div>
            <div v-if="v$.createdAt.$anyDirty && v$.createdAt.$invalid">
              <small class="form-text text-danger" v-for="error of v$.createdAt.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="in-app-notification-read">Read</label>
            <input
              type="checkbox"
              class="form-check"
              name="read"
              id="in-app-notification-read"
              data-cy="read"
              :class="{ valid: !v$.read.$invalid, invalid: v$.read.$invalid }"
              v-model="v$.read.$model"
              required
            />
            <div v-if="v$.read.$anyDirty && v$.read.$invalid">
              <small class="form-text text-danger" v-for="error of v$.read.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="in-app-notification-user">User</label>
            <select class="form-control" id="in-app-notification-user" data-cy="user" name="user" v-model="inAppNotification.user">
              <option :value="null"></option>
              <option
                :value="inAppNotification.user && userOption.id === inAppNotification.user.id ? inAppNotification.user : userOption"
                v-for="userOption in users"
                :key="userOption.id"
              >
                {{ userOption.login }}
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
<script lang="ts" src="./in-app-notification-update.component.ts"></script>
