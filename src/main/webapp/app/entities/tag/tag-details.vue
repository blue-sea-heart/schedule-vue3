<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="tag">
        <h2 class="jh-entity-heading" data-cy="tagDetailsHeading"><span>Tag</span> {{ tag.id }}</h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Name</span>
          </dt>
          <dd>
            <span>{{ tag.name }}</span>
          </dd>
          <dt>
            <span>Color</span>
          </dt>
          <dd>
            <span>{{ tag.color }}</span>
          </dd>
          <dt>
            <span>Schedules</span>
          </dt>
          <dd>
            <span v-for="(schedules, i) in tag.schedules" :key="schedules.id"
              >{{ i > 0 ? ', ' : '' }}
              <router-link :to="{ name: 'ScheduleView', params: { scheduleId: schedules.id } }">{{ schedules.title }}</router-link>
            </span>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>返回</span>
        </button>
        <router-link v-if="tag.id" :to="{ name: 'TagEdit', params: { tagId: tag.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span>编辑</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./tag-details.component.ts"></script>
