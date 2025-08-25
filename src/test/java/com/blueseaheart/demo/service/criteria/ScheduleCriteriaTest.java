package com.blueseaheart.demo.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ScheduleCriteriaTest {

    @Test
    void newScheduleCriteriaHasAllFiltersNullTest() {
        var scheduleCriteria = new ScheduleCriteria();
        assertThat(scheduleCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void scheduleCriteriaFluentMethodsCreatesFiltersTest() {
        var scheduleCriteria = new ScheduleCriteria();

        setAllFilters(scheduleCriteria);

        assertThat(scheduleCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void scheduleCriteriaCopyCreatesNullFilterTest() {
        var scheduleCriteria = new ScheduleCriteria();
        var copy = scheduleCriteria.copy();

        assertThat(scheduleCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(scheduleCriteria)
        );
    }

    @Test
    void scheduleCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var scheduleCriteria = new ScheduleCriteria();
        setAllFilters(scheduleCriteria);

        var copy = scheduleCriteria.copy();

        assertThat(scheduleCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(scheduleCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var scheduleCriteria = new ScheduleCriteria();

        assertThat(scheduleCriteria).hasToString("ScheduleCriteria{}");
    }

    private static void setAllFilters(ScheduleCriteria scheduleCriteria) {
        scheduleCriteria.id();
        scheduleCriteria.title();
        scheduleCriteria.location();
        scheduleCriteria.allDay();
        scheduleCriteria.startTime();
        scheduleCriteria.endTime();
        scheduleCriteria.priority();
        scheduleCriteria.completedAt();
        scheduleCriteria.visibility();
        scheduleCriteria.remindersId();
        scheduleCriteria.ownerId();
        scheduleCriteria.statusId();
        scheduleCriteria.categoryId();
        scheduleCriteria.tagsId();
        scheduleCriteria.distinct();
    }

    private static Condition<ScheduleCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getTitle()) &&
                condition.apply(criteria.getLocation()) &&
                condition.apply(criteria.getAllDay()) &&
                condition.apply(criteria.getStartTime()) &&
                condition.apply(criteria.getEndTime()) &&
                condition.apply(criteria.getPriority()) &&
                condition.apply(criteria.getCompletedAt()) &&
                condition.apply(criteria.getVisibility()) &&
                condition.apply(criteria.getRemindersId()) &&
                condition.apply(criteria.getOwnerId()) &&
                condition.apply(criteria.getStatusId()) &&
                condition.apply(criteria.getCategoryId()) &&
                condition.apply(criteria.getTagsId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ScheduleCriteria> copyFiltersAre(ScheduleCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getTitle(), copy.getTitle()) &&
                condition.apply(criteria.getLocation(), copy.getLocation()) &&
                condition.apply(criteria.getAllDay(), copy.getAllDay()) &&
                condition.apply(criteria.getStartTime(), copy.getStartTime()) &&
                condition.apply(criteria.getEndTime(), copy.getEndTime()) &&
                condition.apply(criteria.getPriority(), copy.getPriority()) &&
                condition.apply(criteria.getCompletedAt(), copy.getCompletedAt()) &&
                condition.apply(criteria.getVisibility(), copy.getVisibility()) &&
                condition.apply(criteria.getRemindersId(), copy.getRemindersId()) &&
                condition.apply(criteria.getOwnerId(), copy.getOwnerId()) &&
                condition.apply(criteria.getStatusId(), copy.getStatusId()) &&
                condition.apply(criteria.getCategoryId(), copy.getCategoryId()) &&
                condition.apply(criteria.getTagsId(), copy.getTagsId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
