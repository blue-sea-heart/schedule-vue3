package com.blueseaheart.demo.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ViewPreferenceCriteriaTest {

    @Test
    void newViewPreferenceCriteriaHasAllFiltersNullTest() {
        var viewPreferenceCriteria = new ViewPreferenceCriteria();
        assertThat(viewPreferenceCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void viewPreferenceCriteriaFluentMethodsCreatesFiltersTest() {
        var viewPreferenceCriteria = new ViewPreferenceCriteria();

        setAllFilters(viewPreferenceCriteria);

        assertThat(viewPreferenceCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void viewPreferenceCriteriaCopyCreatesNullFilterTest() {
        var viewPreferenceCriteria = new ViewPreferenceCriteria();
        var copy = viewPreferenceCriteria.copy();

        assertThat(viewPreferenceCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(viewPreferenceCriteria)
        );
    }

    @Test
    void viewPreferenceCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var viewPreferenceCriteria = new ViewPreferenceCriteria();
        setAllFilters(viewPreferenceCriteria);

        var copy = viewPreferenceCriteria.copy();

        assertThat(viewPreferenceCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(viewPreferenceCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var viewPreferenceCriteria = new ViewPreferenceCriteria();

        assertThat(viewPreferenceCriteria).hasToString("ViewPreferenceCriteria{}");
    }

    private static void setAllFilters(ViewPreferenceCriteria viewPreferenceCriteria) {
        viewPreferenceCriteria.id();
        viewPreferenceCriteria.defaultView();
        viewPreferenceCriteria.lastStart();
        viewPreferenceCriteria.lastEnd();
        viewPreferenceCriteria.weekStart();
        viewPreferenceCriteria.showWeekend();
        viewPreferenceCriteria.userId();
        viewPreferenceCriteria.distinct();
    }

    private static Condition<ViewPreferenceCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDefaultView()) &&
                condition.apply(criteria.getLastStart()) &&
                condition.apply(criteria.getLastEnd()) &&
                condition.apply(criteria.getWeekStart()) &&
                condition.apply(criteria.getShowWeekend()) &&
                condition.apply(criteria.getUserId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ViewPreferenceCriteria> copyFiltersAre(
        ViewPreferenceCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDefaultView(), copy.getDefaultView()) &&
                condition.apply(criteria.getLastStart(), copy.getLastStart()) &&
                condition.apply(criteria.getLastEnd(), copy.getLastEnd()) &&
                condition.apply(criteria.getWeekStart(), copy.getWeekStart()) &&
                condition.apply(criteria.getShowWeekend(), copy.getShowWeekend()) &&
                condition.apply(criteria.getUserId(), copy.getUserId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
