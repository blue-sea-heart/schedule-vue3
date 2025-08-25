package com.blueseaheart.demo.service.dto;

import com.blueseaheart.demo.domain.enumeration.ViewType;
import com.blueseaheart.demo.domain.enumeration.WeekStart;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.blueseaheart.demo.domain.ViewPreference} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ViewPreferenceDTO implements Serializable {

    private Long id;

    @NotNull
    private ViewType defaultView;

    private ZonedDateTime lastStart;

    private ZonedDateTime lastEnd;

    @NotNull
    private WeekStart weekStart;

    @NotNull
    private Boolean showWeekend;

    private UserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ViewType getDefaultView() {
        return defaultView;
    }

    public void setDefaultView(ViewType defaultView) {
        this.defaultView = defaultView;
    }

    public ZonedDateTime getLastStart() {
        return lastStart;
    }

    public void setLastStart(ZonedDateTime lastStart) {
        this.lastStart = lastStart;
    }

    public ZonedDateTime getLastEnd() {
        return lastEnd;
    }

    public void setLastEnd(ZonedDateTime lastEnd) {
        this.lastEnd = lastEnd;
    }

    public WeekStart getWeekStart() {
        return weekStart;
    }

    public void setWeekStart(WeekStart weekStart) {
        this.weekStart = weekStart;
    }

    public Boolean getShowWeekend() {
        return showWeekend;
    }

    public void setShowWeekend(Boolean showWeekend) {
        this.showWeekend = showWeekend;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ViewPreferenceDTO)) {
            return false;
        }

        ViewPreferenceDTO viewPreferenceDTO = (ViewPreferenceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, viewPreferenceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ViewPreferenceDTO{" +
            "id=" + getId() +
            ", defaultView='" + getDefaultView() + "'" +
            ", lastStart='" + getLastStart() + "'" +
            ", lastEnd='" + getLastEnd() + "'" +
            ", weekStart='" + getWeekStart() + "'" +
            ", showWeekend='" + getShowWeekend() + "'" +
            ", user=" + getUser() +
            "}";
    }
}
