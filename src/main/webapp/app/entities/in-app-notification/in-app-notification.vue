<template>
  <div>
    <h2 id="page-heading" data-cy="InAppNotificationHeading">
      <span id="in-app-notification-heading">In App Notifications</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refresh list</span>
        </button>
        <router-link :to="{ name: 'InAppNotificationCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-in-app-notification"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>创建新 In App Notification</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && inAppNotifications && inAppNotifications.length === 0">
      <span>No In App Notifications found</span>
    </div>
    <div class="table-responsive" v-if="inAppNotifications && inAppNotifications.length > 0">
      <table class="table table-striped" aria-describedby="inAppNotifications">
        <thead>
          <tr>
            <th scope="row" @click="changeOrder('id')">
              <span>ID</span> <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('title')">
              <span>Title</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'title'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('content')">
              <span>Content</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'content'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('createdAt')">
              <span>Created At</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'createdAt'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('read')">
              <span>Read</span> <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'read'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('user.login')">
              <span>User</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'user.login'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="inAppNotification in inAppNotifications" :key="inAppNotification.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'InAppNotificationView', params: { inAppNotificationId: inAppNotification.id } }">{{
                inAppNotification.id
              }}</router-link>
            </td>
            <td>{{ inAppNotification.title }}</td>
            <td>{{ inAppNotification.content }}</td>
            <td>{{ formatDateShort(inAppNotification.createdAt) || '' }}</td>
            <td>{{ inAppNotification.read }}</td>
            <td>
              {{ inAppNotification.user ? inAppNotification.user.login : '' }}
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'InAppNotificationView', params: { inAppNotificationId: inAppNotification.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">查看</span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'InAppNotificationEdit', params: { inAppNotificationId: inAppNotification.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">编辑</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(inAppNotification)"
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
        <span id="scheduleVue3App.inAppNotification.delete.question" data-cy="inAppNotificationDeleteDialogHeading">确认删除</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-inAppNotification-heading">你确定要删除 In App Notification {{ removeId }} 吗？</p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">取消</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-inAppNotification"
            data-cy="entityConfirmDeleteButton"
            @click="removeInAppNotification()"
          >
            删除
          </button>
        </div>
      </template>
    </b-modal>
    <div v-show="inAppNotifications && inAppNotifications.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./in-app-notification.component.ts"></script>
