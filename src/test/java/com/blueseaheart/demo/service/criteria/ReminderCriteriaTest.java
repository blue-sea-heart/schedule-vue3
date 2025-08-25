package com.blueseaheart.demo.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ReminderCriteriaTest {

    @Test
    void newReminderCriteriaHasAllFiltersNullTest() {
        var reminderCriteria = new ReminderCriteria();
        assertThat(reminderCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void reminderCriteriaFluentMethodsCreatesFiltersTest() {
        var reminderCriteria = new ReminderCriteria();

        setAllFilters(reminderCriteria);

        assertThat(reminderCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void reminderCriteriaCopyCreatesNullFilterTest() {
        var reminderCriteria = new ReminderCriteria();
        var copy = reminderCriteria.copy();

        assertThat(reminderCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(reminderCriteria)
        );
    }

    @Test
    void reminderCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var reminderCriteria = new ReminderCriteria();
        setAllFilters(reminderCriteria);

        var copy = reminderCriteria.copy();

        assertThat(reminderCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(reminderCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var reminderCriteria = new ReminderCriteria();

        assertThat(reminderCriteria).hasToString("ReminderCriteria{}");
    }

    private static void setAllFilters(ReminderCriteria reminderCriteria) {
        reminderCriteria.id();
        reminderCriteria.remindAt();
        reminderCriteria.channel();
        reminderCriteria.subject();
        reminderCriteria.sent();
        reminderCriteria.attemptCount();
        reminderCriteria.lastAttemptAt();
        reminderCriteria.lastErrorMsg();
        reminderCriteria.errorMsg();
        reminderCriteria.scheduleId();
        reminderCriteria.distinct();
    }

    private static Condition<ReminderCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getRemindAt()) &&
                condition.apply(criteria.getChannel()) &&
                condition.apply(criteria.getSubject()) &&
                condition.apply(criteria.getSent()) &&
                condition.apply(criteria.getAttemptCount()) &&
                condition.apply(criteria.getLastAttemptAt()) &&
                condition.apply(criteria.getLastErrorMsg()) &&
                condition.apply(criteria.getErrorMsg()) &&
                condition.apply(criteria.getScheduleId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ReminderCriteria> copyFiltersAre(ReminderCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getRemindAt(), copy.getRemindAt()) &&
                condition.apply(criteria.getChannel(), copy.getChannel()) &&
                condition.apply(criteria.getSubject(), copy.getSubject()) &&
                condition.apply(criteria.getSent(), copy.getSent()) &&
                condition.apply(criteria.getAttemptCount(), copy.getAttemptCount()) &&
                condition.apply(criteria.getLastAttemptAt(), copy.getLastAttemptAt()) &&
                condition.apply(criteria.getLastErrorMsg(), copy.getLastErrorMsg()) &&
                condition.apply(criteria.getErrorMsg(), copy.getErrorMsg()) &&
                condition.apply(criteria.getScheduleId(), copy.getScheduleId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
