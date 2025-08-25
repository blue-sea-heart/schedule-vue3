<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="schedule">
        <h2 class="jh-entity-heading" data-cy="scheduleDetailsHeading"><span>Schedule</span> {{ schedule.id }}</h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Title</span>
          </dt>
          <dd>
            <span>{{ schedule.title }}</span>
          </dd>
          <dt>
            <span>Description</span>
          </dt>
          <dd>
            <span>{{ schedule.description }}</span>
          </dd>
          <dt>
            <span>Location</span>
          </dt>
          <dd>
            <span>{{ schedule.location }}</span>
          </dd>
          <dt>
            <span>All Day</span>
          </dt>
          <dd>
            <span>{{ schedule.allDay }}</span>
          </dd>
          <dt>
            <span>Start Time</span>
          </dt>
          <dd>
            <span v-if="schedule.startTime">{{ formatDateLong(schedule.startTime) }}</span>
          </dd>
          <dt>
            <span>End Time</span>
          </dt>
          <dd>
            <span v-if="schedule.endTime">{{ formatDateLong(schedule.endTime) }}</span>
          </dd>
          <dt>
            <span>Priority</span>
          </dt>
          <dd>
            <span>{{ schedule.priority }}</span>
          </dd>
          <dt>
            <span>Completed At</span>
          </dt>
          <dd>
            <span v-if="schedule.completedAt">{{ formatDateLong(schedule.completedAt) }}</span>
          </dd>
          <dt>
            <span>Visibility</span>
          </dt>
          <dd>
            <span>{{ schedule.visibility }}</span>
          </dd>
          <dt>
            <span>Owner</span>
          </dt>
          <dd>
            {{ schedule.owner ? schedule.owner.login : '' }}
          </dd>
          <dt>
            <span>Status</span>
          </dt>
          <dd>
            <div v-if="schedule.status">
              <router-link :to="{ name: 'ScheduleStatusView', params: { scheduleStatusId: schedule.status.id } }">{{
                schedule.status.name
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>Category</span>
          </dt>
          <dd>
            <div v-if="schedule.category">
              <router-link :to="{ name: 'CategoryView', params: { categoryId: schedule.category.id } }">{{
                schedule.category.name
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>Tags</span>
          </dt>
          <dd>
            <span v-for="(tags, i) in schedule.tags" :key="tags.id"
              >{{ i > 0 ? ', ' : '' }}
              <router-link :to="{ name: 'TagView', params: { tagId: tags.id } }">{{ tags.name }}</router-link>
            </span>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>返回</span>
        </button>
        <router-link v-if="schedule.id" :to="{ name: 'ScheduleEdit', params: { scheduleId: schedule.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span>编辑</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./schedule-details.component.ts"></script>
