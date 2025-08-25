package com.blueseaheart.demo.service.criteria;

import com.blueseaheart.demo.domain.enumeration.ReminderChannel;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.blueseaheart.demo.domain.Reminder} entity. This class is used
 * in {@link com.blueseaheart.demo.web.rest.ReminderResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /reminders?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReminderCriteria implements Serializable, Criteria {

    /**
     * Class for filtering ReminderChannel
     */
    public static class ReminderChannelFilter extends Filter<ReminderChannel> {

        public ReminderChannelFilter() {}

        public ReminderChannelFilter(ReminderChannelFilter filter) {
            super(filter);
        }

        @Override
        public ReminderChannelFilter copy() {
            return new ReminderChannelFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private ZonedDateTimeFilter remindAt;

    private ReminderChannelFilter channel;

    private StringFilter subject;

    private BooleanFilter sent;

    private IntegerFilter attemptCount;

    private ZonedDateTimeFilter lastAttemptAt;

    private StringFilter lastErrorMsg;

    private StringFilter errorMsg;

    private LongFilter scheduleId;

    private Boolean distinct;

    public ReminderCriteria() {}

    public ReminderCriteria(ReminderCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.remindAt = other.optionalRemindAt().map(ZonedDateTimeFilter::copy).orElse(null);
        this.channel = other.optionalChannel().map(ReminderChannelFilter::copy).orElse(null);
        this.subject = other.optionalSubject().map(StringFilter::copy).orElse(null);
        this.sent = other.optionalSent().map(BooleanFilter::copy).orElse(null);
        this.attemptCount = other.optionalAttemptCount().map(IntegerFilter::copy).orElse(null);
        this.lastAttemptAt = other.optionalLastAttemptAt().map(ZonedDateTimeFilter::copy).orElse(null);
        this.lastErrorMsg = other.optionalLastErrorMsg().map(StringFilter::copy).orElse(null);
        this.errorMsg = other.optionalErrorMsg().map(StringFilter::copy).orElse(null);
        this.scheduleId = other.optionalScheduleId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ReminderCriteria copy() {
        return new ReminderCriteria(this);
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

    public ZonedDateTimeFilter getRemindAt() {
        return remindAt;
    }

    public Optional<ZonedDateTimeFilter> optionalRemindAt() {
        return Optional.ofNullable(remindAt);
    }

    public ZonedDateTimeFilter remindAt() {
        if (remindAt == null) {
            setRemindAt(new ZonedDateTimeFilter());
        }
        return remindAt;
    }

    public void setRemindAt(ZonedDateTimeFilter remindAt) {
        this.remindAt = remindAt;
    }

    public ReminderChannelFilter getChannel() {
        return channel;
    }

    public Optional<ReminderChannelFilter> optionalChannel() {
        return Optional.ofNullable(channel);
    }

    public ReminderChannelFilter channel() {
        if (channel == null) {
            setChannel(new ReminderChannelFilter());
        }
        return channel;
    }

    public void setChannel(ReminderChannelFilter channel) {
        this.channel = channel;
    }

    public StringFilter getSubject() {
        return subject;
    }

    public Optional<StringFilter> optionalSubject() {
        return Optional.ofNullable(subject);
    }

    public StringFilter subject() {
        if (subject == null) {
            setSubject(new StringFilter());
        }
        return subject;
    }

    public void setSubject(StringFilter subject) {
        this.subject = subject;
    }

    public BooleanFilter getSent() {
        return sent;
    }

    public Optional<BooleanFilter> optionalSent() {
        return Optional.ofNullable(sent);
    }

    public BooleanFilter sent() {
        if (sent == null) {
            setSent(new BooleanFilter());
        }
        return sent;
    }

    public void setSent(BooleanFilter sent) {
        this.sent = sent;
    }

    public IntegerFilter getAttemptCount() {
        return attemptCount;
    }

    public Optional<IntegerFilter> optionalAttemptCount() {
        return Optional.ofNullable(attemptCount);
    }

    public IntegerFilter attemptCount() {
        if (attemptCount == null) {
            setAttemptCount(new IntegerFilter());
        }
        return attemptCount;
    }

    public void setAttemptCount(IntegerFilter attemptCount) {
        this.attemptCount = attemptCount;
    }

    public ZonedDateTimeFilter getLastAttemptAt() {
        return lastAttemptAt;
    }

    public Optional<ZonedDateTimeFilter> optionalLastAttemptAt() {
        return Optional.ofNullable(lastAttemptAt);
    }

    public ZonedDateTimeFilter lastAttemptAt() {
        if (lastAttemptAt == null) {
            setLastAttemptAt(new ZonedDateTimeFilter());
        }
        return lastAttemptAt;
    }

    public void setLastAttemptAt(ZonedDateTimeFilter lastAttemptAt) {
        this.lastAttemptAt = lastAttemptAt;
    }

    public StringFilter getLastErrorMsg() {
        return lastErrorMsg;
    }

    public Optional<StringFilter> optionalLastErrorMsg() {
        return Optional.ofNullable(lastErrorMsg);
    }

    public StringFilter lastErrorMsg() {
        if (lastErrorMsg == null) {
            setLastErrorMsg(new StringFilter());
        }
        return lastErrorMsg;
    }

    public void setLastErrorMsg(StringFilter lastErrorMsg) {
        this.lastErrorMsg = lastErrorMsg;
    }

    public StringFilter getErrorMsg() {
        return errorMsg;
    }

    public Optional<StringFilter> optionalErrorMsg() {
        return Optional.ofNullable(errorMsg);
    }

    public StringFilter errorMsg() {
        if (errorMsg == null) {
            setErrorMsg(new StringFilter());
        }
        return errorMsg;
    }

    public void setErrorMsg(StringFilter errorMsg) {
        this.errorMsg = errorMsg;
    }

    public LongFilter getScheduleId() {
        return scheduleId;
    }

    public Optional<LongFilter> optionalScheduleId() {
        return Optional.ofNullable(scheduleId);
    }

    public LongFilter scheduleId() {
        if (scheduleId == null) {
            setScheduleId(new LongFilter());
        }
        return scheduleId;
    }

    public void setScheduleId(LongFilter scheduleId) {
        this.scheduleId = scheduleId;
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
        final ReminderCriteria that = (ReminderCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(remindAt, that.remindAt) &&
            Objects.equals(channel, that.channel) &&
            Objects.equals(subject, that.subject) &&
            Objects.equals(sent, that.sent) &&
            Objects.equals(attemptCount, that.attemptCount) &&
            Objects.equals(lastAttemptAt, that.lastAttemptAt) &&
            Objects.equals(lastErrorMsg, that.lastErrorMsg) &&
            Objects.equals(errorMsg, that.errorMsg) &&
            Objects.equals(scheduleId, that.scheduleId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            remindAt,
            channel,
            subject,
            sent,
            attemptCount,
            lastAttemptAt,
            lastErrorMsg,
            errorMsg,
            scheduleId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReminderCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalRemindAt().map(f -> "remindAt=" + f + ", ").orElse("") +
            optionalChannel().map(f -> "channel=" + f + ", ").orElse("") +
            optionalSubject().map(f -> "subject=" + f + ", ").orElse("") +
            optionalSent().map(f -> "sent=" + f + ", ").orElse("") +
            optionalAttemptCount().map(f -> "attemptCount=" + f + ", ").orElse("") +
            optionalLastAttemptAt().map(f -> "lastAttemptAt=" + f + ", ").orElse("") +
            optionalLastErrorMsg().map(f -> "lastErrorMsg=" + f + ", ").orElse("") +
            optionalErrorMsg().map(f -> "errorMsg=" + f + ", ").orElse("") +
            optionalScheduleId().map(f -> "scheduleId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
