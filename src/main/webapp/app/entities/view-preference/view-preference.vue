<template>
  <div>
    <h2 id="page-heading" data-cy="ViewPreferenceHeading">
      <span id="view-preference-heading">View Preferences</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refresh list</span>
        </button>
        <router-link :to="{ name: 'ViewPreferenceCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-view-preference"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>创建新 View Preference</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && viewPreferences && viewPreferences.length === 0">
      <span>No View Preferences found</span>
    </div>
    <div class="table-responsive" v-if="viewPreferences && viewPreferences.length > 0">
      <table class="table table-striped" aria-describedby="viewPreferences">
        <thead>
          <tr>
            <th scope="row" @click="changeOrder('id')">
              <span>ID</span> <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('defaultView')">
              <span>Default View</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'defaultView'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('lastStart')">
              <span>Last Start</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'lastStart'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('lastEnd')">
              <span>Last End</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'lastEnd'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('weekStart')">
              <span>Week Start</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'weekStart'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('showWeekend')">
              <span>Show Weekend</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'showWeekend'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('user.login')">
              <span>User</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'user.login'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="viewPreference in viewPreferences" :key="viewPreference.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'ViewPreferenceView', params: { viewPreferenceId: viewPreference.id } }">{{
                viewPreference.id
              }}</router-link>
            </td>
            <td>{{ viewPreference.defaultView }}</td>
            <td>{{ formatDateShort(viewPreference.lastStart) || '' }}</td>
            <td>{{ formatDateShort(viewPreference.lastEnd) || '' }}</td>
            <td>{{ viewPreference.weekStart }}</td>
            <td>{{ viewPreference.showWeekend }}</td>
            <td>
              {{ viewPreference.user ? viewPreference.user.login : '' }}
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'ViewPreferenceView', params: { viewPreferenceId: viewPreference.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">查看</span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'ViewPreferenceEdit', params: { viewPreferenceId: viewPreference.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">编辑</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(viewPreference)"
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
        <span id="scheduleVue3App.viewPreference.delete.question" data-cy="viewPreferenceDeleteDialogHeading">确认删除</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-viewPreference-heading">你确定要删除 View Preference {{ removeId }} 吗？</p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">取消</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-viewPreference"
            data-cy="entityConfirmDeleteButton"
            @click="removeViewPreference()"
          >
            删除
          </button>
        </div>
      </template>
    </b-modal>
    <div v-show="viewPreferences && viewPreferences.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./view-preference.component.ts"></script>
