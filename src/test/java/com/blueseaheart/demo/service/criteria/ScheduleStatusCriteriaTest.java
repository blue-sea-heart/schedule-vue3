package com.blueseaheart.demo.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ScheduleStatusCriteriaTest {

    @Test
    void newScheduleStatusCriteriaHasAllFiltersNullTest() {
        var scheduleStatusCriteria = new ScheduleStatusCriteria();
        assertThat(scheduleStatusCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void scheduleStatusCriteriaFluentMethodsCreatesFiltersTest() {
        var scheduleStatusCriteria = new ScheduleStatusCriteria();

        setAllFilters(scheduleStatusCriteria);

        assertThat(scheduleStatusCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void scheduleStatusCriteriaCopyCreatesNullFilterTest() {
        var scheduleStatusCriteria = new ScheduleStatusCriteria();
        var copy = scheduleStatusCriteria.copy();

        assertThat(scheduleStatusCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(scheduleStatusCriteria)
        );
    }

    @Test
    void scheduleStatusCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var scheduleStatusCriteria = new ScheduleStatusCriteria();
        setAllFilters(scheduleStatusCriteria);

        var copy = scheduleStatusCriteria.copy();

        assertThat(scheduleStatusCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(scheduleStatusCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var scheduleStatusCriteria = new ScheduleStatusCriteria();

        assertThat(scheduleStatusCriteria).hasToString("ScheduleStatusCriteria{}");
    }

    private static void setAllFilters(ScheduleStatusCriteria scheduleStatusCriteria) {
        scheduleStatusCriteria.id();
        scheduleStatusCriteria.code();
        scheduleStatusCriteria.name();
        scheduleStatusCriteria.color();
        scheduleStatusCriteria.sortNo();
        scheduleStatusCriteria.isDefault();
        scheduleStatusCriteria.isTerminal();
        scheduleStatusCriteria.distinct();
    }

    private static Condition<ScheduleStatusCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getCode()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getColor()) &&
                condition.apply(criteria.getSortNo()) &&
                condition.apply(criteria.getIsDefault()) &&
                condition.apply(criteria.getIsTerminal()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ScheduleStatusCriteria> copyFiltersAre(
        ScheduleStatusCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getCode(), copy.getCode()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getColor(), copy.getColor()) &&
                condition.apply(criteria.getSortNo(), copy.getSortNo()) &&
                condition.apply(criteria.getIsDefault(), copy.getIsDefault()) &&
                condition.apply(criteria.getIsTerminal(), copy.getIsTerminal()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
