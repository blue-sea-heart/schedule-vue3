package com.blueseaheart.demo.service.dto;

import com.blueseaheart.demo.domain.enumeration.Priority;
import com.blueseaheart.demo.domain.enumeration.Visibility;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.blueseaheart.demo.domain.Schedule} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ScheduleDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 140)
    private String title;

    @Lob
    private String description;

    @Size(max = 140)
    private String location;

    @NotNull
    private Boolean allDay;

    @NotNull
    private ZonedDateTime startTime;

    private ZonedDateTime endTime;

    @NotNull
    private Priority priority;

    private ZonedDateTime completedAt;

    @NotNull
    private Visibility visibility;

    private UserDTO owner;

    private ScheduleStatusDTO status;

    private CategoryDTO category;

    private Set<TagDTO> tags = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Boolean getAllDay() {
        return allDay;
    }

    public void setAllDay(Boolean allDay) {
        this.allDay = allDay;
    }

    public ZonedDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(ZonedDateTime startTime) {
        this.startTime = startTime;
    }

    public ZonedDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(ZonedDateTime endTime) {
        this.endTime = endTime;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public ZonedDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(ZonedDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    public UserDTO getOwner() {
        return owner;
    }

    public void setOwner(UserDTO owner) {
        this.owner = owner;
    }

    public ScheduleStatusDTO getStatus() {
        return status;
    }

    public void setStatus(ScheduleStatusDTO status) {
        this.status = status;
    }

    public CategoryDTO getCategory() {
        return category;
    }

    public void setCategory(CategoryDTO category) {
        this.category = category;
    }

    public Set<TagDTO> getTags() {
        return tags;
    }

    public void setTags(Set<TagDTO> tags) {
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ScheduleDTO)) {
            return false;
        }

        ScheduleDTO scheduleDTO = (ScheduleDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, scheduleDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ScheduleDTO{" +
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
            ", owner=" + getOwner() +
            ", status=" + getStatus() +
            ", category=" + getCategory() +
            ", tags=" + getTags() +
            "}";
    }
}
