<template>
  <div>
    <h2 id="page-heading" data-cy="ScheduleStatusHeading">
      <span id="schedule-status-heading">Schedule Statuses</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refresh list</span>
        </button>
        <router-link :to="{ name: 'ScheduleStatusCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-schedule-status"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>创建新 Schedule Status</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && scheduleStatuses && scheduleStatuses.length === 0">
      <span>No Schedule Statuses found</span>
    </div>
    <div class="table-responsive" v-if="scheduleStatuses && scheduleStatuses.length > 0">
      <table class="table table-striped" aria-describedby="scheduleStatuses">
        <thead>
          <tr>
            <th scope="row" @click="changeOrder('id')">
              <span>ID</span> <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('code')">
              <span>Code</span> <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'code'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('name')">
              <span>Name</span> <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'name'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('color')">
              <span>Color</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'color'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('sortNo')">
              <span>Sort No</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'sortNo'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('isDefault')">
              <span>Is Default</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'isDefault'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('isTerminal')">
              <span>Is Terminal</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'isTerminal'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="scheduleStatus in scheduleStatuses" :key="scheduleStatus.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'ScheduleStatusView', params: { scheduleStatusId: scheduleStatus.id } }">{{
                scheduleStatus.id
              }}</router-link>
            </td>
            <td>{{ scheduleStatus.code }}</td>
            <td>{{ scheduleStatus.name }}</td>
            <td>{{ scheduleStatus.color }}</td>
            <td>{{ scheduleStatus.sortNo }}</td>
            <td>{{ scheduleStatus.isDefault }}</td>
            <td>{{ scheduleStatus.isTerminal }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'ScheduleStatusView', params: { scheduleStatusId: scheduleStatus.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">查看</span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'ScheduleStatusEdit', params: { scheduleStatusId: scheduleStatus.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">编辑</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(scheduleStatus)"
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
        <span id="scheduleVue3App.scheduleStatus.delete.question" data-cy="scheduleStatusDeleteDialogHeading">确认删除</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-scheduleStatus-heading">你确定要删除 Schedule Status {{ removeId }} 吗？</p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">取消</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-scheduleStatus"
            data-cy="entityConfirmDeleteButton"
            @click="removeScheduleStatus()"
          >
            删除
          </button>
        </div>
      </template>
    </b-modal>
    <div v-show="scheduleStatuses && scheduleStatuses.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./schedule-status.component.ts"></script>
