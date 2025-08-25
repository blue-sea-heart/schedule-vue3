package com.blueseaheart.demo.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.blueseaheart.demo.domain.ScheduleStatus} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ScheduleStatusDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 40)
    private String code;

    @NotNull
    @Size(max = 60)
    private String name;

    @Size(max = 20)
    private String color;

    @NotNull
    private Integer sortNo;

    @NotNull
    private Boolean isDefault;

    @NotNull
    private Boolean isTerminal;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getSortNo() {
        return sortNo;
    }

    public void setSortNo(Integer sortNo) {
        this.sortNo = sortNo;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public Boolean getIsTerminal() {
        return isTerminal;
    }

    public void setIsTerminal(Boolean isTerminal) {
        this.isTerminal = isTerminal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ScheduleStatusDTO)) {
            return false;
        }

        ScheduleStatusDTO scheduleStatusDTO = (ScheduleStatusDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, scheduleStatusDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ScheduleStatusDTO{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", color='" + getColor() + "'" +
            ", sortNo=" + getSortNo() +
            ", isDefault='" + getIsDefault() + "'" +
            ", isTerminal='" + getIsTerminal() + "'" +
            "}";
    }
}
