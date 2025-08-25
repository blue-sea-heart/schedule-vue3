package com.blueseaheart.demo.domain;

import com.blueseaheart.demo.domain.enumeration.ReminderChannel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Reminder.
 */
@Entity
@Table(name = "reminder")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Reminder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "remind_at", nullable = false)
    private ZonedDateTime remindAt;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "channel", nullable = false)
    private ReminderChannel channel;

    @Size(max = 140)
    @Column(name = "subject", length = 140)
    private String subject;

    @Lob
    @Column(name = "content")
    private String content;

    @NotNull
    @Column(name = "sent", nullable = false)
    private Boolean sent;

    @Column(name = "attempt_count")
    private Integer attemptCount;

    @Column(name = "last_attempt_at")
    private ZonedDateTime lastAttemptAt;

    @Size(max = 500)
    @Column(name = "last_error_msg", length = 500)
    private String lastErrorMsg;

    @Size(max = 500)
    @Column(name = "error_msg", length = 500)
    private String errorMsg;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "reminders", "owner", "status", "category", "tags" }, allowSetters = true)
    private Schedule schedule;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Reminder id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getRemindAt() {
        return this.remindAt;
    }

    public Reminder remindAt(ZonedDateTime remindAt) {
        this.setRemindAt(remindAt);
        return this;
    }

    public void setRemindAt(ZonedDateTime remindAt) {
        this.remindAt = remindAt;
    }

    public ReminderChannel getChannel() {
        return this.channel;
    }

    public Reminder channel(ReminderChannel channel) {
        this.setChannel(channel);
        return this;
    }

    public void setChannel(ReminderChannel channel) {
        this.channel = channel;
    }

    public String getSubject() {
        return this.subject;
    }

    public Reminder subject(String subject) {
        this.setSubject(subject);
        return this;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return this.content;
    }

    public Reminder content(String content) {
        this.setContent(content);
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getSent() {
        return this.sent;
    }

    public Reminder sent(Boolean sent) {
        this.setSent(sent);
        return this;
    }

    public void setSent(Boolean sent) {
        this.sent = sent;
    }

    public Integer getAttemptCount() {
        return this.attemptCount;
    }

    public Reminder attemptCount(Integer attemptCount) {
        this.setAttemptCount(attemptCount);
        return this;
    }

    public void setAttemptCount(Integer attemptCount) {
        this.attemptCount = attemptCount;
    }

    public ZonedDateTime getLastAttemptAt() {
        return this.lastAttemptAt;
    }

    public Reminder lastAttemptAt(ZonedDateTime lastAttemptAt) {
        this.setLastAttemptAt(lastAttemptAt);
        return this;
    }

    public void setLastAttemptAt(ZonedDateTime lastAttemptAt) {
        this.lastAttemptAt = lastAttemptAt;
    }

    public String getLastErrorMsg() {
        return this.lastErrorMsg;
    }

    public Reminder lastErrorMsg(String lastErrorMsg) {
        this.setLastErrorMsg(lastErrorMsg);
        return this;
    }

    public void setLastErrorMsg(String lastErrorMsg) {
        this.lastErrorMsg = lastErrorMsg;
    }

    public String getErrorMsg() {
        return this.errorMsg;
    }

    public Reminder errorMsg(String errorMsg) {
        this.setErrorMsg(errorMsg);
        return this;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Schedule getSchedule() {
        return this.schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public Reminder schedule(Schedule schedule) {
        this.setSchedule(schedule);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Reminder)) {
            return false;
        }
        return getId() != null && getId().equals(((Reminder) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Reminder{" +
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
            "}";
    }
}
