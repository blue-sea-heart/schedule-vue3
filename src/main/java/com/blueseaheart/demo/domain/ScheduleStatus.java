package com.blueseaheart.demo.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ScheduleStatus.
 */
@Entity
@Table(name = "schedule_status")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ScheduleStatus implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 40)
    @Column(name = "code", length = 40, nullable = false, unique = true)
    private String code;

    @NotNull
    @Size(max = 60)
    @Column(name = "name", length = 60, nullable = false, unique = true)
    private String name;

    @Size(max = 20)
    @Column(name = "color", length = 20)
    private String color;

    @NotNull
    @Column(name = "sort_no", nullable = false)
    private Integer sortNo;

    @NotNull
    @Column(name = "is_default", nullable = false)
    private Boolean isDefault;

    @NotNull
    @Column(name = "is_terminal", nullable = false)
    private Boolean isTerminal;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ScheduleStatus id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public ScheduleStatus code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public ScheduleStatus name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return this.color;
    }

    public ScheduleStatus color(String color) {
        this.setColor(color);
        return this;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getSortNo() {
        return this.sortNo;
    }

    public ScheduleStatus sortNo(Integer sortNo) {
        this.setSortNo(sortNo);
        return this;
    }

    public void setSortNo(Integer sortNo) {
        this.sortNo = sortNo;
    }

    public Boolean getIsDefault() {
        return this.isDefault;
    }

    public ScheduleStatus isDefault(Boolean isDefault) {
        this.setIsDefault(isDefault);
        return this;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public Boolean getIsTerminal() {
        return this.isTerminal;
    }

    public ScheduleStatus isTerminal(Boolean isTerminal) {
        this.setIsTerminal(isTerminal);
        return this;
    }

    public void setIsTerminal(Boolean isTerminal) {
        this.isTerminal = isTerminal;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ScheduleStatus)) {
            return false;
        }
        return getId() != null && getId().equals(((ScheduleStatus) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ScheduleStatus{" +
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
