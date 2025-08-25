package com.blueseaheart.demo.domain;

import com.blueseaheart.demo.domain.enumeration.Priority;
import com.blueseaheart.demo.domain.enumeration.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Schedule.
 */
@Entity
@Table(name = "schedule")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Schedule implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 140)
    @Column(name = "title", length = 140, nullable = false)
    private String title;

    @Lob
    @Column(name = "description")
    private String description;

    @Size(max = 140)
    @Column(name = "location", length = 140)
    private String location;

    @NotNull
    @Column(name = "all_day", nullable = false)
    private Boolean allDay;

    @NotNull
    @Column(name = "start_time", nullable = false)
    private ZonedDateTime startTime;

    @Column(name = "end_time")
    private ZonedDateTime endTime;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false)
    private Priority priority;

    @Column(name = "completed_at")
    private ZonedDateTime completedAt;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "visibility", nullable = false)
    private Visibility visibility;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "schedule")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "schedule" }, allowSetters = true)
    private Set<Reminder> reminders = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private User owner;

    @ManyToOne(fetch = FetchType.LAZY)
    private ScheduleStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_schedule__tags",
        joinColumns = @JoinColumn(name = "schedule_id"),
        inverseJoinColumns = @JoinColumn(name = "tags_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "schedules" }, allowSetters = true)
    private Set<Tag> tags = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Schedule id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public Schedule title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public Schedule description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return this.location;
    }

    public Schedule location(String location) {
        this.setLocation(location);
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Boolean getAllDay() {
        return this.allDay;
    }

    public Schedule allDay(Boolean allDay) {
        this.setAllDay(allDay);
        return this;
    }

    public void setAllDay(Boolean allDay) {
        this.allDay = allDay;
    }

    public ZonedDateTime getStartTime() {
        return this.startTime;
    }

    public Schedule startTime(ZonedDateTime startTime) {
        this.setStartTime(startTime);
        return this;
    }

    public void setStartTime(ZonedDateTime startTime) {
        this.startTime = startTime;
    }

    public ZonedDateTime getEndTime() {
        return this.endTime;
    }

    public Schedule endTime(ZonedDateTime endTime) {
        this.setEndTime(endTime);
        return this;
    }

    public void setEndTime(ZonedDateTime endTime) {
        this.endTime = endTime;
    }

    public Priority getPriority() {
        return this.priority;
    }

    public Schedule priority(Priority priority) {
        this.setPriority(priority);
        return this;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public ZonedDateTime getCompletedAt() {
        return this.completedAt;
    }

    public Schedule completedAt(ZonedDateTime completedAt) {
        this.setCompletedAt(completedAt);
        return this;
    }

    public void setCompletedAt(ZonedDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public Visibility getVisibility() {
        return this.visibility;
    }

    public Schedule visibility(Visibility visibility) {
        this.setVisibility(visibility);
        return this;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    public Set<Reminder> getReminders() {
        return this.reminders;
    }

    public void setReminders(Set<Reminder> reminders) {
        if (this.reminders != null) {
            this.reminders.forEach(i -> i.setSchedule(null));
        }
        if (reminders != null) {
            reminders.forEach(i -> i.setSchedule(this));
        }
        this.reminders = reminders;
    }

    public Schedule reminders(Set<Reminder> reminders) {
        this.setReminders(reminders);
        return this;
    }

    public Schedule addReminders(Reminder reminder) {
        this.reminders.add(reminder);
        reminder.setSchedule(this);
        return this;
    }

    public Schedule removeReminders(Reminder reminder) {
        this.reminders.remove(reminder);
        reminder.setSchedule(null);
        return this;
    }

    public User getOwner() {
        return this.owner;
    }

    public void setOwner(User user) {
        this.owner = user;
    }

    public Schedule owner(User user) {
        this.setOwner(user);
        return this;
    }

    public ScheduleStatus getStatus() {
        return this.status;
    }

    public void setStatus(ScheduleStatus scheduleStatus) {
        this.status = scheduleStatus;
    }

    public Schedule status(ScheduleStatus scheduleStatus) {
        this.setStatus(scheduleStatus);
        return this;
    }

    public Category getCategory() {
        return this.category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Schedule category(Category category) {
        this.setCategory(category);
        return this;
    }

    public Set<Tag> getTags() {
        return this.tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Schedule tags(Set<Tag> tags) {
        this.setTags(tags);
        return this;
    }

    public Schedule addTags(Tag tag) {
        this.tags.add(tag);
        return this;
    }

    public Schedule removeTags(Tag tag) {
        this.tags.remove(tag);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Schedule)) {
            return false;
        }
        return getId() != null && getId().equals(((Schedule) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Schedule{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", location='" + getLocation() + "'" +
            ", allDay='" + getAllDay() + "'" +
            ", startTime='" + getStartTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            ", priority='" + getPriority() + "'" +
            ", completedAt='" + getCompletedAt() + "'" +
            ", visibility='" + getVisibility() + "'" +
            "}";
    }
}
