<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="scheduleVue3App.scheduleStatus.home.createOrEditLabel" data-cy="ScheduleStatusCreateUpdateHeading">
          创建或编辑 Schedule Status
        </h2>
        <div>
          <div class="form-group" v-if="scheduleStatus.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="scheduleStatus.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="schedule-status-code">Code</label>
            <input
              type="text"
              class="form-control"
              name="code"
              id="schedule-status-code"
              data-cy="code"
              :class="{ valid: !v$.code.$invalid, invalid: v$.code.$invalid }"
              v-model="v$.code.$model"
              required
            />
            <div v-if="v$.code.$anyDirty && v$.code.$invalid">
              <small class="form-text text-danger" v-for="error of v$.code.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="schedule-status-name">Name</label>
            <input
              type="text"
              class="form-control"
              name="name"
              id="schedule-status-name"
              data-cy="name"
              :class="{ valid: !v$.name.$invalid, invalid: v$.name.$invalid }"
              v-model="v$.name.$model"
              required
            />
            <div v-if="v$.name.$anyDirty && v$.name.$invalid">
              <small class="form-text text-danger" v-for="error of v$.name.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="schedule-status-color">Color</label>
            <input
              type="text"
              class="form-control"
              name="color"
              id="schedule-status-color"
              data-cy="color"
              :class="{ valid: !v$.color.$invalid, invalid: v$.color.$invalid }"
              v-model="v$.color.$model"
            />
            <div v-if="v$.color.$anyDirty && v$.color.$invalid">
              <small class="form-text text-danger" v-for="error of v$.color.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="schedule-status-sortNo">Sort No</label>
            <input
              type="number"
              class="form-control"
              name="sortNo"
              id="schedule-status-sortNo"
              data-cy="sortNo"
              :class="{ valid: !v$.sortNo.$invalid, invalid: v$.sortNo.$invalid }"
              v-model.number="v$.sortNo.$model"
              required
            />
            <div v-if="v$.sortNo.$anyDirty && v$.sortNo.$invalid">
              <small class="form-text text-danger" v-for="error of v$.sortNo.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="schedule-status-isDefault">Is Default</label>
            <input
              type="checkbox"
              class="form-check"
              name="isDefault"
              id="schedule-status-isDefault"
              data-cy="isDefault"
              :class="{ valid: !v$.isDefault.$invalid, invalid: v$.isDefault.$invalid }"
              v-model="v$.isDefault.$model"
              required
            />
            <div v-if="v$.isDefault.$anyDirty && v$.isDefault.$invalid">
              <small class="form-text text-danger" v-for="error of v$.isDefault.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="schedule-status-isTerminal">Is Terminal</label>
            <input
              type="checkbox"
              class="form-check"
              name="isTerminal"
              id="schedule-status-isTerminal"
              data-cy="isTerminal"
              :class="{ valid: !v$.isTerminal.$invalid, invalid: v$.isTerminal.$invalid }"
              v-model="v$.isTerminal.$model"
              required
            />
            <div v-if="v$.isTerminal.$anyDirty && v$.isTerminal.$invalid">
              <small class="form-text text-danger" v-for="error of v$.isTerminal.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
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
<script lang="ts" src="./schedule-status-update.component.ts"></script>
