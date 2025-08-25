package com.blueseaheart.demo.service.criteria;

import com.blueseaheart.demo.domain.enumeration.ViewType;
import com.blueseaheart.demo.domain.enumeration.WeekStart;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.blueseaheart.demo.domain.ViewPreference} entity. This class is used
 * in {@link com.blueseaheart.demo.web.rest.ViewPreferenceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /view-preferences?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ViewPreferenceCriteria implements Serializable, Criteria {

    /**
     * Class for filtering ViewType
     */
    public static class ViewTypeFilter extends Filter<ViewType> {

        public ViewTypeFilter() {}

        public ViewTypeFilter(ViewTypeFilter filter) {
            super(filter);
        }

        @Override
        public ViewTypeFilter copy() {
            return new ViewTypeFilter(this);
        }
    }

    /**
     * Class for filtering WeekStart
     */
    public static class WeekStartFilter extends Filter<WeekStart> {

        public WeekStartFilter() {}

        public WeekStartFilter(WeekStartFilter filter) {
            super(filter);
        }

        @Override
        public WeekStartFilter copy() {
            return new WeekStartFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private ViewTypeFilter defaultView;

    private ZonedDateTimeFilter lastStart;

    private ZonedDateTimeFilter lastEnd;

    private WeekStartFilter weekStart;

    private BooleanFilter showWeekend;

    private LongFilter userId;

    private Boolean distinct;

    public ViewPreferenceCriteria() {}

    public ViewPreferenceCriteria(ViewPreferenceCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.defaultView = other.optionalDefaultView().map(ViewTypeFilter::copy).orElse(null);
        this.lastStart = other.optionalLastStart().map(ZonedDateTimeFilter::copy).orElse(null);
        this.lastEnd = other.optionalLastEnd().map(ZonedDateTimeFilter::copy).orElse(null);
        this.weekStart = other.optionalWeekStart().map(WeekStartFilter::copy).orElse(null);
        this.showWeekend = other.optionalShowWeekend().map(BooleanFilter::copy).orElse(null);
        this.userId = other.optionalUserId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ViewPreferenceCriteria copy() {
        return new ViewPreferenceCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public ViewTypeFilter getDefaultView() {
        return defaultView;
    }

    public Optional<ViewTypeFilter> optionalDefaultView() {
        return Optional.ofNullable(defaultView);
    }

    public ViewTypeFilter defaultView() {
        if (defaultView == null) {
            setDefaultView(new ViewTypeFilter());
        }
        return defaultView;
    }

    public void setDefaultView(ViewTypeFilter defaultView) {
        this.defaultView = defaultView;
    }

    public ZonedDateTimeFilter getLastStart() {
        return lastStart;
    }

    public Optional<ZonedDateTimeFilter> optionalLastStart() {
        return Optional.ofNullable(lastStart);
    }

    public ZonedDateTimeFilter lastStart() {
        if (lastStart == null) {
            setLastStart(new ZonedDateTimeFilter());
        }
        return lastStart;
    }

    public void setLastStart(ZonedDateTimeFilter lastStart) {
        this.lastStart = lastStart;
    }

    public ZonedDateTimeFilter getLastEnd() {
        return lastEnd;
    }

    public Optional<ZonedDateTimeFilter> optionalLastEnd() {
        return Optional.ofNullable(lastEnd);
    }

    public ZonedDateTimeFilter lastEnd() {
        if (lastEnd == null) {
            setLastEnd(new ZonedDateTimeFilter());
        }
        return lastEnd;
    }

    public void setLastEnd(ZonedDateTimeFilter lastEnd) {
        this.lastEnd = lastEnd;
    }

    public WeekStartFilter getWeekStart() {
        return weekStart;
    }

    public Optional<WeekStartFilter> optionalWeekStart() {
        return Optional.ofNullable(weekStart);
    }

    public WeekStartFilter weekStart() {
        if (weekStart == null) {
            setWeekStart(new WeekStartFilter());
        }
        return weekStart;
    }

    public void setWeekStart(WeekStartFilter weekStart) {
        this.weekStart = weekStart;
    }

    public BooleanFilter getShowWeekend() {
        return showWeekend;
    }

    public Optional<BooleanFilter> optionalShowWeekend() {
        return Optional.ofNullable(showWeekend);
    }

    public BooleanFilter showWeekend() {
        if (showWeekend == null) {
            setShowWeekend(new BooleanFilter());
        }
        return showWeekend;
    }

    public void setShowWeekend(BooleanFilter showWeekend) {
        this.showWeekend = showWeekend;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public Optional<LongFilter> optionalUserId() {
        return Optional.ofNullable(userId);
    }

    public LongFilter userId() {
        if (userId == null) {
            setUserId(new LongFilter());
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ViewPreferenceCriteria that = (ViewPreferenceCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(defaultView, that.defaultView) &&
            Objects.equals(lastStart, that.lastStart) &&
            Objects.equals(lastEnd, that.lastEnd) &&
            Objects.equals(weekStart, that.weekStart) &&
            Objects.equals(showWeekend, that.showWeekend) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, defaultView, lastStart, lastEnd, weekStart, showWeekend, userId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ViewPreferenceCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDefaultView().map(f -> "defaultView=" + f + ", ").orElse("") +
            optionalLastStart().map(f -> "lastStart=" + f + ", ").orElse("") +
            optionalLastEnd().map(f -> "lastEnd=" + f + ", ").orElse("") +
            optionalWeekStart().map(f -> "weekStart=" + f + ", ").orElse("") +
            optionalShowWeekend().map(f -> "showWeekend=" + f + ", ").orElse("") +
            optionalUserId().map(f -> "userId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
