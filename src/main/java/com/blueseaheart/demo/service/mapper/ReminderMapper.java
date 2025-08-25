package com.blueseaheart.demo.service.mapper;

import com.blueseaheart.demo.domain.Reminder;
import com.blueseaheart.demo.domain.Schedule;
import com.blueseaheart.demo.service.dto.ReminderDTO;
import com.blueseaheart.demo.service.dto.ScheduleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Reminder} and its DTO {@link ReminderDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReminderMapper extends EntityMapper<ReminderDTO, Reminder> {
    @Mapping(target = "schedule", source = "schedule", qualifiedByName = "scheduleId")
    ReminderDTO toDto(Reminder s);

    @Named("scheduleId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ScheduleDTO toDtoScheduleId(Schedule schedule);
}
