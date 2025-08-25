package com.blueseaheart.demo.service.mapper;

import com.blueseaheart.demo.domain.Schedule;
import com.blueseaheart.demo.domain.Tag;
import com.blueseaheart.demo.service.dto.ScheduleDTO;
import com.blueseaheart.demo.service.dto.TagDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Tag} and its DTO {@link TagDTO}.
 */
@Mapper(componentModel = "spring")
public interface TagMapper extends EntityMapper<TagDTO, Tag> {
    @Mapping(target = "schedules", source = "schedules", qualifiedByName = "scheduleTitleSet")
    TagDTO toDto(Tag s);

    @Mapping(target = "schedules", ignore = true)
    @Mapping(target = "removeSchedules", ignore = true)
    Tag toEntity(TagDTO tagDTO);

    @Named("scheduleTitle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    ScheduleDTO toDtoScheduleTitle(Schedule schedule);

    @Named("scheduleTitleSet")
    default Set<ScheduleDTO> toDtoScheduleTitleSet(Set<Schedule> schedule) {
        return schedule.stream().map(this::toDtoScheduleTitle).collect(Collectors.toSet());
    }
}
