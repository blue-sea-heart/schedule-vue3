<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="scheduleVue3App.viewPreference.home.createOrEditLabel" data-cy="ViewPreferenceCreateUpdateHeading">
          创建或编辑 View Preference
        </h2>
        <div>
          <div class="form-group" v-if="viewPreference.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="viewPreference.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="view-preference-defaultView">Default View</label>
            <select
              class="form-control"
              name="defaultView"
              :class="{ valid: !v$.defaultView.$invalid, invalid: v$.defaultView.$invalid }"
              v-model="v$.defaultView.$model"
              id="view-preference-defaultView"
              data-cy="defaultView"
              required
            >
              <option v-for="viewType in viewTypeValues" :key="viewType" :value="viewType">{{ viewType }}</option>
            </select>
            <div v-if="v$.defaultView.$anyDirty && v$.defaultView.$invalid">
              <small class="form-text text-danger" v-for="error of v$.defaultView.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="view-preference-lastStart">Last Start</label>
            <div class="d-flex">
              <input
                id="view-preference-lastStart"
                data-cy="lastStart"
                type="datetime-local"
                class="form-control"
                name="lastStart"
                :class="{ valid: !v$.lastStart.$invalid, invalid: v$.lastStart.$invalid }"
                :value="convertDateTimeFromServer(v$.lastStart.$model)"
                @change="updateZonedDateTimeField('lastStart', $event)"
              />
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="view-preference-lastEnd">Last End</label>
            <div class="d-flex">
              <input
                id="view-preference-lastEnd"
                data-cy="lastEnd"
                type="datetime-local"
                class="form-control"
                name="lastEnd"
                :class="{ valid: !v$.lastEnd.$invalid, invalid: v$.lastEnd.$invalid }"
                :value="convertDateTimeFromServer(v$.lastEnd.$model)"
                @change="updateZonedDateTimeField('lastEnd', $event)"
              />
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="view-preference-weekStart">Week Start</label>
            <select
              class="form-control"
              name="weekStart"
              :class="{ valid: !v$.weekStart.$invalid, invalid: v$.weekStart.$invalid }"
              v-model="v$.weekStart.$model"
              id="view-preference-weekStart"
              data-cy="weekStart"
              required
            >
              <option v-for="weekStart in weekStartValues" :key="weekStart" :value="weekStart">{{ weekStart }}</option>
            </select>
            <div v-if="v$.weekStart.$anyDirty && v$.weekStart.$invalid">
              <small class="form-text text-danger" v-for="error of v$.weekStart.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="view-preference-showWeekend">Show Weekend</label>
            <input
              type="checkbox"
              class="form-check"
              name="showWeekend"
              id="view-preference-showWeekend"
              data-cy="showWeekend"
              :class="{ valid: !v$.showWeekend.$invalid, invalid: v$.showWeekend.$invalid }"
              v-model="v$.showWeekend.$model"
              required
            />
            <div v-if="v$.showWeekend.$anyDirty && v$.showWeekend.$invalid">
              <small class="form-text text-danger" v-for="error of v$.showWeekend.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="view-preference-user">User</label>
            <select class="form-control" id="view-preference-user" data-cy="user" name="user" v-model="viewPreference.user">
              <option :value="null"></option>
              <option
                :value="viewPreference.user && userOption.id === viewPreference.user.id ? viewPreference.user : userOption"
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
<script lang="ts" src="./view-preference-update.component.ts"></script>
