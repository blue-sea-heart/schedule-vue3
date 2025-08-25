package com.blueseaheart.demo.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.blueseaheart.demo.domain.InAppNotification} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InAppNotificationDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 140)
    private String title;

    @Lob
    private String content;

    @NotNull
    private ZonedDateTime createdAt;

    @NotNull
    private Boolean read;

    private UserDTO user;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getRead() {
        return read;
    }

    public void setRead(Boolean read) {
        this.read = read;
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
        if (!(o instanceof InAppNotificationDTO)) {
            return false;
        }

        InAppNotificationDTO inAppNotificationDTO = (InAppNotificationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, inAppNotificationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InAppNotificationDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", content='" + getContent() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", read='" + getRead() + "'" +
            ", user=" + getUser() +
            "}";
    }
}
