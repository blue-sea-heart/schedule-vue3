<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="scheduleStatus">
        <h2 class="jh-entity-heading" data-cy="scheduleStatusDetailsHeading"><span>Schedule Status</span> {{ scheduleStatus.id }}</h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Code</span>
          </dt>
          <dd>
            <span>{{ scheduleStatus.code }}</span>
          </dd>
          <dt>
            <span>Name</span>
          </dt>
          <dd>
            <span>{{ scheduleStatus.name }}</span>
          </dd>
          <dt>
            <span>Color</span>
          </dt>
          <dd>
            <span>{{ scheduleStatus.color }}</span>
          </dd>
          <dt>
            <span>Sort No</span>
          </dt>
          <dd>
            <span>{{ scheduleStatus.sortNo }}</span>
          </dd>
          <dt>
            <span>Is Default</span>
          </dt>
          <dd>
            <span>{{ scheduleStatus.isDefault }}</span>
          </dd>
          <dt>
            <span>Is Terminal</span>
          </dt>
          <dd>
            <span>{{ scheduleStatus.isTerminal }}</span>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>返回</span>
        </button>
        <router-link
          v-if="scheduleStatus.id"
          :to="{ name: 'ScheduleStatusEdit', params: { scheduleStatusId: scheduleStatus.id } }"
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

<script lang="ts" src="./schedule-status-details.component.ts"></script>
