<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="scheduleVue3App.tag.home.createOrEditLabel" data-cy="TagCreateUpdateHeading">创建或编辑 Tag</h2>
        <div>
          <div class="form-group" v-if="tag.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="tag.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="tag-name">Name</label>
            <input
              type="text"
              class="form-control"
              name="name"
              id="tag-name"
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
            <label class="form-control-label" for="tag-color">Color</label>
            <input
              type="text"
              class="form-control"
              name="color"
              id="tag-color"
              data-cy="color"
              :class="{ valid: !v$.color.$invalid, invalid: v$.color.$invalid }"
              v-model="v$.color.$model"
            />
            <div v-if="v$.color.$anyDirty && v$.color.$invalid">
              <small class="form-text text-danger" v-for="error of v$.color.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label for="tag-schedules">Schedules</label>
            <select
              class="form-control"
              id="tag-schedules"
              data-cy="schedules"
              multiple
              name="schedules"
              v-if="tag.schedules !== undefined"
              v-model="tag.schedules"
            >
              <option
                :value="getSelected(tag.schedules, scheduleOption, 'id')"
                v-for="scheduleOption in schedules"
                :key="scheduleOption.id"
              >
                {{ scheduleOption.title }}
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
<script lang="ts" src="./tag-update.component.ts"></script>
