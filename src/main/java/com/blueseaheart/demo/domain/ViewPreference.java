package com.blueseaheart.demo.domain;

import com.blueseaheart.demo.domain.enumeration.ViewType;
import com.blueseaheart.demo.domain.enumeration.WeekStart;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ViewPreference.
 */
@Entity
@Table(name = "view_preference")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ViewPreference implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "default_view", nullable = false)
    private ViewType defaultView;

    @Column(name = "last_start")
    private ZonedDateTime lastStart;

    @Column(name = "last_end")
    private ZonedDateTime lastEnd;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "week_start", nullable = false)
    private WeekStart weekStart;

    @NotNull
    @Column(name = "show_weekend", nullable = false)
    private Boolean showWeekend;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ViewPreference id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ViewType getDefaultView() {
        return this.defaultView;
    }

    public ViewPreference defaultView(ViewType defaultView) {
        this.setDefaultView(defaultView);
        return this;
    }

    public void setDefaultView(ViewType defaultView) {
        this.defaultView = defaultView;
    }

    public ZonedDateTime getLastStart() {
        return this.lastStart;
    }

    public ViewPreference lastStart(ZonedDateTime lastStart) {
        this.setLastStart(lastStart);
        return this;
    }

    public void setLastStart(ZonedDateTime lastStart) {
        this.lastStart = lastStart;
    }

    public ZonedDateTime getLastEnd() {
        return this.lastEnd;
    }

    public ViewPreference lastEnd(ZonedDateTime lastEnd) {
        this.setLastEnd(lastEnd);
        return this;
    }

    public void setLastEnd(ZonedDateTime lastEnd) {
        this.lastEnd = lastEnd;
    }

    public WeekStart getWeekStart() {
        return this.weekStart;
    }

    public ViewPreference weekStart(WeekStart weekStart) {
        this.setWeekStart(weekStart);
        return this;
    }

    public void setWeekStart(WeekStart weekStart) {
        this.weekStart = weekStart;
    }

    public Boolean getShowWeekend() {
        return this.showWeekend;
    }

    public ViewPreference showWeekend(Boolean showWeekend) {
        this.setShowWeekend(showWeekend);
        return this;
    }

    public void setShowWeekend(Boolean showWeekend) {
        this.showWeekend = showWeekend;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ViewPreference user(User user) {
        this.setUser(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ViewPreference)) {
            return false;
        }
        return getId() != null && getId().equals(((ViewPreference) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ViewPreference{" +
            "id=" + getId() +
            ", defaultView='" + getDefaultView() + "'" +
            ", lastStart='" + getLastStart() + "'" +
            ", lastEnd='" + getLastEnd() + "'" +
            ", weekStart='" + getWeekStart() + "'" +
            ", showWeekend='" + getShowWeekend() + "'" +
            "}";
    }
}
