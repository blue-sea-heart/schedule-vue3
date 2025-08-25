package com.blueseaheart.demo.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.blueseaheart.demo.domain.ScheduleStatus} entity. This class is used
 * in {@link com.blueseaheart.demo.web.rest.ScheduleStatusResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /schedule-statuses?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ScheduleStatusCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter code;

    private StringFilter name;

    private StringFilter color;

    private IntegerFilter sortNo;

    private BooleanFilter isDefault;

    private BooleanFilter isTerminal;

    private Boolean distinct;

    public ScheduleStatusCriteria() {}

    public ScheduleStatusCriteria(ScheduleStatusCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.code = other.optionalCode().map(StringFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.color = other.optionalColor().map(StringFilter::copy).orElse(null);
        this.sortNo = other.optionalSortNo().map(IntegerFilter::copy).orElse(null);
        this.isDefault = other.optionalIsDefault().map(BooleanFilter::copy).orElse(null);
        this.isTerminal = other.optionalIsTerminal().map(BooleanFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ScheduleStatusCriteria copy() {
        return new ScheduleStatusCriteria(this);
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

    public StringFilter getCode() {
        return code;
    }

    public Optional<StringFilter> optionalCode() {
        return Optional.ofNullable(code);
    }

    public StringFilter code() {
        if (code == null) {
            setCode(new StringFilter());
        }
        return code;
    }

    public void setCode(StringFilter code) {
        this.code = code;
    }

    public StringFilter getName() {
        return name;
    }

    public Optional<StringFilter> optionalName() {
        return Optional.ofNullable(name);
    }

    public StringFilter name() {
        if (name == null) {
            setName(new StringFilter());
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getColor() {
        return color;
    }

    public Optional<StringFilter> optionalColor() {
        return Optional.ofNullable(color);
    }

    public StringFilter color() {
        if (color == null) {
            setColor(new StringFilter());
        }
        return color;
    }

    public void setColor(StringFilter color) {
        this.color = color;
    }

    public IntegerFilter getSortNo() {
        return sortNo;
    }

    public Optional<IntegerFilter> optionalSortNo() {
        return Optional.ofNullable(sortNo);
    }

    public IntegerFilter sortNo() {
        if (sortNo == null) {
            setSortNo(new IntegerFilter());
        }
        return sortNo;
    }

    public void setSortNo(IntegerFilter sortNo) {
        this.sortNo = sortNo;
    }

    public BooleanFilter getIsDefault() {
        return isDefault;
    }

    public Optional<BooleanFilter> optionalIsDefault() {
        return Optional.ofNullable(isDefault);
    }

    public BooleanFilter isDefault() {
        if (isDefault == null) {
            setIsDefault(new BooleanFilter());
        }
        return isDefault;
    }

    public void setIsDefault(BooleanFilter isDefault) {
        this.isDefault = isDefault;
    }

    public BooleanFilter getIsTerminal() {
        return isTerminal;
    }

    public Optional<BooleanFilter> optionalIsTerminal() {
        return Optional.ofNullable(isTerminal);
    }

    public BooleanFilter isTerminal() {
        if (isTerminal == null) {
            setIsTerminal(new BooleanFilter());
        }
        return isTerminal;
    }

    public void setIsTerminal(BooleanFilter isTerminal) {
        this.isTerminal = isTerminal;
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
        final ScheduleStatusCriteria that = (ScheduleStatusCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(code, that.code) &&
            Objects.equals(name, that.name) &&
            Objects.equals(color, that.color) &&
            Objects.equals(sortNo, that.sortNo) &&
            Objects.equals(isDefault, that.isDefault) &&
            Objects.equals(isTerminal, that.isTerminal) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, name, color, sortNo, isDefault, isTerminal, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ScheduleStatusCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalCode().map(f -> "code=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalColor().map(f -> "color=" + f + ", ").orElse("") +
            optionalSortNo().map(f -> "sortNo=" + f + ", ").orElse("") +
            optionalIsDefault().map(f -> "isDefault=" + f + ", ").orElse("") +
            optionalIsTerminal().map(f -> "isTerminal=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
