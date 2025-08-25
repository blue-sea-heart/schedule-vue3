package com.blueseaheart.demo.service.dto;

import com.blueseaheart.demo.domain.enumeration.ReminderChannel;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.blueseaheart.demo.domain.Reminder} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReminderDTO implements Serializable {

    private Long id;

    @NotNull
    private ZonedDateTime remindAt;

    @NotNull
    private ReminderChannel channel;

    @Size(max = 140)
    private String subject;

    @Lob
    private String content;

    @NotNull
    private Boolean sent;

    private Integer attemptCount;

    private ZonedDateTime lastAttemptAt;

    @Size(max = 500)
    private String lastErrorMsg;

    @Size(max = 500)
    private String errorMsg;

    private ScheduleDTO schedule;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getRemindAt() {
        return remindAt;
    }

    public void setRemindAt(ZonedDateTime remindAt) {
        this.remindAt = remindAt;
    }

    public ReminderChannel getChannel() {
        return channel;
    }

    public void setChannel(ReminderChannel channel) {
        this.channel = channel;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getSent() {
        return sent;
    }

    public void setSent(Boolean sent) {
        this.sent = sent;
    }

    public Integer getAttemptCount() {
        return attemptCount;
    }

    public void setAttemptCount(Integer attemptCount) {
        this.attemptCount = attemptCount;
    }

    public ZonedDateTime getLastAttemptAt() {
        return lastAttemptAt;
    }

    public void setLastAttemptAt(ZonedDateTime lastAttemptAt) {
        this.lastAttemptAt = lastAttemptAt;
    }

    public String getLastErrorMsg() {
        return lastErrorMsg;
    }

    public void setLastErrorMsg(String lastErrorMsg) {
        this.lastErrorMsg = lastErrorMsg;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public ScheduleDTO getSchedule() {
        return schedule;
    }

    public void setSchedule(ScheduleDTO schedule) {
        this.schedule = schedule;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReminderDTO)) {
            return false;
        }

        ReminderDTO reminderDTO = (ReminderDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, reminderDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReminderDTO{" +
            "id=" + getId() +
            ", remindAt='" + getRemindAt() + "'" +
            ", channel='" + getChannel() + "'" +
            ", subject='" + getSubject() + "'" +
            ", content='" + getContent() + "'" +
            ", sent='" + getSent() + "'" +
            ", attemptCount=" + getAttemptCount() +
            ", lastAttemptAt='" + getLastAttemptAt() + "'" +
            ", lastErrorMsg='" + getLastErrorMsg() + "'" +
            ", errorMsg='" + getErrorMsg() + "'" +
            ", schedule=" + getSchedule() +
            "}";
    }
}
