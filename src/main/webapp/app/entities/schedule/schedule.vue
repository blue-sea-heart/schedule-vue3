<template>
  <div>
    <h2 id="page-heading" data-cy="ScheduleHeading">
      <span id="schedule-heading">Schedules</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refresh list</span>
        </button>
        <router-link :to="{ name: 'ScheduleCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-schedule"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>创建新 Schedule</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && schedules && schedules.length === 0">
      <span>No Schedules found</span>
    </div>
    <div class="table-responsive" v-if="schedules && schedules.length > 0">
      <table class="table table-striped" aria-describedby="schedules">
        <thead>
          <tr>
            <th scope="row" @click="changeOrder('id')">
              <span>ID</span> <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('title')">
              <span>Title</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'title'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('description')">
              <span>Description</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'description'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('location')">
              <span>Location</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'location'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('allDay')">
              <span>All Day</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'allDay'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('startTime')">
              <span>Start Time</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'startTime'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('endTime')">
              <span>End Time</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'endTime'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('priority')">
              <span>Priority</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'priority'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('completedAt')">
              <span>Completed At</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'completedAt'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('visibility')">
              <span>Visibility</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'visibility'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('owner.login')">
              <span>Owner</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'owner.login'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('status.name')">
              <span>Status</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'status.name'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('category.name')">
              <span>Category</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'category.name'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="schedule in schedules" :key="schedule.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'ScheduleView', params: { scheduleId: schedule.id } }">{{ schedule.id }}</router-link>
            </td>
            <td>{{ schedule.title }}</td>
            <td>{{ schedule.description }}</td>
            <td>{{ schedule.location }}</td>
            <td>{{ schedule.allDay }}</td>
            <td>{{ formatDateShort(schedule.startTime) || '' }}</td>
            <td>{{ formatDateShort(schedule.endTime) || '' }}</td>
            <td>{{ schedule.priority }}</td>
            <td>{{ formatDateShort(schedule.completedAt) || '' }}</td>
            <td>{{ schedule.visibility }}</td>
            <td>
              {{ schedule.owner ? schedule.owner.login : '' }}
            </td>
            <td>
              <div v-if="schedule.status">
                <router-link :to="{ name: 'ScheduleStatusView', params: { scheduleStatusId: schedule.status.id } }">{{
                  schedule.status.name
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="schedule.category">
                <router-link :to="{ name: 'CategoryView', params: { categoryId: schedule.category.id } }">{{
                  schedule.category.name
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'ScheduleView', params: { scheduleId: schedule.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">查看</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'ScheduleEdit', params: { scheduleId: schedule.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">编辑</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(schedule)"
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
        <span id="scheduleVue3App.schedule.delete.question" data-cy="scheduleDeleteDialogHeading">确认删除</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-schedule-heading">你确定要删除 Schedule {{ removeId }} 吗？</p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">取消</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-schedule"
            data-cy="entityConfirmDeleteButton"
            @click="removeSchedule()"
          >
            删除
          </button>
        </div>
      </template>
    </b-modal>
    <div v-show="schedules && schedules.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./schedule.component.ts"></script>
