package com.blueseaheart.demo.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class InAppNotificationCriteriaTest {

    @Test
    void newInAppNotificationCriteriaHasAllFiltersNullTest() {
        var inAppNotificationCriteria = new InAppNotificationCriteria();
        assertThat(inAppNotificationCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void inAppNotificationCriteriaFluentMethodsCreatesFiltersTest() {
        var inAppNotificationCriteria = new InAppNotificationCriteria();

        setAllFilters(inAppNotificationCriteria);

        assertThat(inAppNotificationCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void inAppNotificationCriteriaCopyCreatesNullFilterTest() {
        var inAppNotificationCriteria = new InAppNotificationCriteria();
        var copy = inAppNotificationCriteria.copy();

        assertThat(inAppNotificationCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(inAppNotificationCriteria)
        );
    }

    @Test
    void inAppNotificationCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var inAppNotificationCriteria = new InAppNotificationCriteria();
        setAllFilters(inAppNotificationCriteria);

        var copy = inAppNotificationCriteria.copy();

        assertThat(inAppNotificationCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(inAppNotificationCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var inAppNotificationCriteria = new InAppNotificationCriteria();

        assertThat(inAppNotificationCriteria).hasToString("InAppNotificationCriteria{}");
    }

    private static void setAllFilters(InAppNotificationCriteria inAppNotificationCriteria) {
        inAppNotificationCriteria.id();
        inAppNotificationCriteria.title();
        inAppNotificationCriteria.createdAt();
        inAppNotificationCriteria.read();
        inAppNotificationCriteria.userId();
        inAppNotificationCriteria.distinct();
    }

    private static Condition<InAppNotificationCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getTitle()) &&
                condition.apply(criteria.getCreatedAt()) &&
                condition.apply(criteria.getRead()) &&
                condition.apply(criteria.getUserId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<InAppNotificationCriteria> copyFiltersAre(
        InAppNotificationCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getTitle(), copy.getTitle()) &&
                condition.apply(criteria.getCreatedAt(), copy.getCreatedAt()) &&
                condition.apply(criteria.getRead(), copy.getRead()) &&
                condition.apply(criteria.getUserId(), copy.getUserId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
