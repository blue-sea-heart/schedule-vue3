<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="inAppNotification">
        <h2 class="jh-entity-heading" data-cy="inAppNotificationDetailsHeading">
          <span>In App Notification</span> {{ inAppNotification.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Title</span>
          </dt>
          <dd>
            <span>{{ inAppNotification.title }}</span>
          </dd>
          <dt>
            <span>Content</span>
          </dt>
          <dd>
            <span>{{ inAppNotification.content }}</span>
          </dd>
          <dt>
            <span>Created At</span>
          </dt>
          <dd>
            <span v-if="inAppNotification.createdAt">{{ formatDateLong(inAppNotification.createdAt) }}</span>
          </dd>
          <dt>
            <span>Read</span>
          </dt>
          <dd>
            <span>{{ inAppNotification.read }}</span>
          </dd>
          <dt>
            <span>User</span>
          </dt>
          <dd>
            {{ inAppNotification.user ? inAppNotification.user.login : '' }}
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>返回</span>
        </button>
        <router-link
          v-if="inAppNotification.id"
          :to="{ name: 'InAppNotificationEdit', params: { inAppNotificationId: inAppNotification.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span>编辑</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./in-app-notification-details.component.ts"></script>
