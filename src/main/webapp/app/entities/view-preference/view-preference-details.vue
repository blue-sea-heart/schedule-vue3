<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="viewPreference">
        <h2 class="jh-entity-heading" data-cy="viewPreferenceDetailsHeading"><span>View Preference</span> {{ viewPreference.id }}</h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Default View</span>
          </dt>
          <dd>
            <span>{{ viewPreference.defaultView }}</span>
          </dd>
          <dt>
            <span>Last Start</span>
          </dt>
          <dd>
            <span v-if="viewPreference.lastStart">{{ formatDateLong(viewPreference.lastStart) }}</span>
          </dd>
          <dt>
            <span>Last End</span>
          </dt>
          <dd>
            <span v-if="viewPreference.lastEnd">{{ formatDateLong(viewPreference.lastEnd) }}</span>
          </dd>
          <dt>
            <span>Week Start</span>
          </dt>
          <dd>
            <span>{{ viewPreference.weekStart }}</span>
          </dd>
          <dt>
            <span>Show Weekend</span>
          </dt>
          <dd>
            <span>{{ viewPreference.showWeekend }}</span>
          </dd>
          <dt>
            <span>User</span>
          </dt>
          <dd>
            {{ viewPreference.user ? viewPreference.user.login : '' }}
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>返回</span>
        </button>
        <router-link
          v-if="viewPreference.id"
          :to="{ name: 'ViewPreferenceEdit', params: { viewPreferenceId: viewPreference.id } }"
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

<script lang="ts" src="./view-preference-details.component.ts"></script>
