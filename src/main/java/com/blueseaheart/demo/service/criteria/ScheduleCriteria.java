package com.blueseaheart.demo.service.criteria;

import com.blueseaheart.demo.domain.enumeration.Priority;
import com.blueseaheart.demo.domain.enumeration.Visibility;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.blueseaheart.demo.domain.Schedule} entity. This class is used
 * in {@link com.blueseaheart.demo.web.rest.ScheduleResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /schedules?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ScheduleCriteria implements Serializable, Criteria {

    /**
     * Class for filtering Priority
     */
    public static class PriorityFilter extends Filter<Priority> {

        public PriorityFilter() {}

        public PriorityFilter(PriorityFilter filter) {
            super(filter);
        }

        @Override
        public PriorityFilter copy() {
            return new PriorityFilter(this);
        }
    }

    /**
     * Class for filtering Visibility
     */
    public static class VisibilityFilter extends Filter<Visibility> {

        public VisibilityFilter() {}

        public VisibilityFilter(VisibilityFilter filter) {
            super(filter);
        }

        @Override
        public VisibilityFilter copy() {
            return new VisibilityFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter title;

    private StringFilter location;

    private BooleanFilter allDay;

    private ZonedDateTimeFilter startTime;

    private ZonedDateTimeFilter endTime;

    private PriorityFilter priority;

    private ZonedDateTimeFilter completedAt;

    private VisibilityFilter visibility;

    private LongFilter remindersId;

    private LongFilter ownerId;

    private LongFilter statusId;

    private LongFilter categoryId;

    private LongFilter tagsId;

    private Boolean distinct;

    public ScheduleCriteria() {}

    public ScheduleCriteria(ScheduleCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.title = other.optionalTitle().map(StringFilter::copy).orElse(null);
        this.location = other.optionalLocation().map(StringFilter::copy).orElse(null);
        this.allDay = other.optionalAllDay().map(BooleanFilter::copy).orElse(null);
        this.startTime = other.optionalStartTime().map(ZonedDateTimeFilter::copy).orElse(null);
        this.endTime = other.optionalEndTime().map(ZonedDateTimeFilter::copy).orElse(null);
        this.priority = other.optionalPriority().map(PriorityFilter::copy).orElse(null);
        this.completedAt = other.optionalCompletedAt().map(ZonedDateTimeFilter::copy).orElse(null);
        this.visibility = other.optionalVisibility().map(VisibilityFilter::copy).orElse(null);
        this.remindersId = other.optionalRemindersId().map(LongFilter::copy).orElse(null);
        this.ownerId = other.optionalOwnerId().map(LongFilter::copy).orElse(null);
        this.statusId = other.optionalStatusId().map(LongFilter::copy).orElse(null);
        this.categoryId = other.optionalCategoryId().map(LongFilter::copy).orElse(null);
        this.tagsId = other.optionalTagsId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ScheduleCriteria copy() {
        return new ScheduleCriteria(this);
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

    public StringFilter getTitle() {
        return title;
    }

    public Optional<StringFilter> optionalTitle() {
        return Optional.ofNullable(title);
    }

    public StringFilter title() {
        if (title == null) {
            setTitle(new StringFilter());
        }
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public StringFilter getLocation() {
        return location;
    }

    public Optional<StringFilter> optionalLocation() {
        return Optional.ofNullable(location);
    }

    public StringFilter location() {
        if (location == null) {
            setLocation(new StringFilter());
        }
        return location;
    }

    public void setLocation(StringFilter location) {
        this.location = location;
    }

    public BooleanFilter getAllDay() {
        return allDay;
    }

    public Optional<BooleanFilter> optionalAllDay() {
        return Optional.ofNullable(allDay);
    }

    public BooleanFilter allDay() {
        if (allDay == null) {
            setAllDay(new BooleanFilter());
        }
        return allDay;
    }

    public void setAllDay(BooleanFilter allDay) {
        this.allDay = allDay;
    }

    public ZonedDateTimeFilter getStartTime() {
        return startTime;
    }

    public Optional<ZonedDateTimeFilter> optionalStartTime() {
        return Optional.ofNullable(startTime);
    }

    public ZonedDateTimeFilter startTime() {
        if (startTime == null) {
            setStartTime(new ZonedDateTimeFilter());
        }
        return startTime;
    }

    public void setStartTime(ZonedDateTimeFilter startTime) {
        this.startTime = startTime;
    }

    public ZonedDateTimeFilter getEndTime() {
        return endTime;
    }

    public Optional<ZonedDateTimeFilter> optionalEndTime() {
        return Optional.ofNullable(endTime);
    }

    public ZonedDateTimeFilter endTime() {
        if (endTime == null) {
            setEndTime(new ZonedDateTimeFilter());
        }
        return endTime;
    }

    public void setEndTime(ZonedDateTimeFilter endTime) {
        this.endTime = endTime;
    }

    public PriorityFilter getPriority() {
        return priority;
    }

    public Optional<PriorityFilter> optionalPriority() {
        return Optional.ofNullable(priority);
    }

    public PriorityFilter priority() {
        if (priority == null) {
            setPriority(new PriorityFilter());
        }
        return priority;
    }

    public void setPriority(PriorityFilter priority) {
        this.priority = priority;
    }

    public ZonedDateTimeFilter getCompletedAt() {
        return completedAt;
    }

    public Optional<ZonedDateTimeFilter> optionalCompletedAt() {
        return Optional.ofNullable(completedAt);
    }

    public ZonedDateTimeFilter completedAt() {
        if (completedAt == null) {
            setCompletedAt(new ZonedDateTimeFilter());
        }
        return completedAt;
    }

    public void setCompletedAt(ZonedDateTimeFilter completedAt) {
        this.completedAt = completedAt;
    }

    public VisibilityFilter getVisibility() {
        return visibility;
    }

    public Optional<VisibilityFilter> optionalVisibility() {
        return Optional.ofNullable(visibility);
    }

    public VisibilityFilter visibility() {
        if (visibility == null) {
            setVisibility(new VisibilityFilter());
        }
        return visibility;
    }

    public void setVisibility(VisibilityFilter visibility) {
        this.visibility = visibility;
    }

    public LongFilter getRemindersId() {
        return remindersId;
    }

    public Optional<LongFilter> optionalRemindersId() {
        return Optional.ofNullable(remindersId);
    }

    public LongFilter remindersId() {
        if (remindersId == null) {
            setRemindersId(new LongFilter());
        }
        return remindersId;
    }

    public void setRemindersId(LongFilter remindersId) {
        this.remindersId = remindersId;
    }

    public LongFilter getOwnerId() {
        return ownerId;
    }

    public Optional<LongFilter> optionalOwnerId() {
        return Optional.ofNullable(ownerId);
    }

    public LongFilter ownerId() {
        if (ownerId == null) {
            setOwnerId(new LongFilter());
        }
        return ownerId;
    }

    public void setOwnerId(LongFilter ownerId) {
        this.ownerId = ownerId;
    }

    public LongFilter getStatusId() {
        return statusId;
    }

    public Optional<LongFilter> optionalStatusId() {
        return Optional.ofNullable(statusId);
    }

    public LongFilter statusId() {
        if (statusId == null) {
            setStatusId(new LongFilter());
        }
        return statusId;
    }

    public void setStatusId(LongFilter statusId) {
        this.statusId = statusId;
    }

    public LongFilter getCategoryId() {
        return categoryId;
    }

    public Optional<LongFilter> optionalCategoryId() {
        return Optional.ofNullable(categoryId);
    }

    public LongFilter categoryId() {
        if (categoryId == null) {
            setCategoryId(new LongFilter());
        }
        return categoryId;
    }

    public void setCategoryId(LongFilter categoryId) {
        this.categoryId = categoryId;
    }

    public LongFilter getTagsId() {
        return tagsId;
    }

    public Optional<LongFilter> optionalTagsId() {
        return Optional.ofNullable(tagsId);
    }

    public LongFilter tagsId() {
        if (tagsId == null) {
            setTagsId(new LongFilter());
        }
        return tagsId;
    }

    public void setTagsId(LongFilter tagsId) {
        this.tagsId = tagsId;
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
        final ScheduleCriteria that = (ScheduleCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(title, that.title) &&
            Objects.equals(location, that.location) &&
            Objects.equals(allDay, that.allDay) &&
            Objects.equals(startTime, that.startTime) &&
            Objects.equals(endTime, that.endTime) &&
            Objects.equals(priority, that.priority) &&
            Objects.equals(completedAt, that.completedAt) &&
            Objects.equals(visibility, that.visibility) &&
            Objects.equals(remindersId, that.remindersId) &&
            Objects.equals(ownerId, that.ownerId) &&
            Objects.equals(statusId, that.statusId) &&
            Objects.equals(categoryId, that.categoryId) &&
            Objects.equals(tagsId, that.tagsId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            title,
            location,
            allDay,
            startTime,
            endTime,
            priority,
            completedAt,
            visibility,
            remindersId,
            ownerId,
            statusId,
            categoryId,
            tagsId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ScheduleCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalTitle().map(f -> "title=" + f + ", ").orElse("") +
            optionalLocation().map(f -> "location=" + f + ", ").orElse("") +
            optionalAllDay().map(f -> "allDay=" + f + ", ").orElse("") +
            optionalStartTime().map(f -> "startTime=" + f + ", ").orElse("") +
            optionalEndTime().map(f -> "endTime=" + f + ", ").orElse("") +
            optionalPriority().map(f -> "priority=" + f + ", ").orElse("") +
            optionalCompletedAt().map(f -> "completedAt=" + f + ", ").orElse("") +
            optionalVisibility().map(f -> "visibility=" + f + ", ").orElse("") +
            optionalRemindersId().map(f -> "remindersId=" + f + ", ").orElse("") +
            optionalOwnerId().map(f -> "ownerId=" + f + ", ").orElse("") +
            optionalStatusId().map(f -> "statusId=" + f + ", ").orElse("") +
            optionalCategoryId().map(f -> "categoryId=" + f + ", ").orElse("") +
            optionalTagsId().map(f -> "tagsId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
