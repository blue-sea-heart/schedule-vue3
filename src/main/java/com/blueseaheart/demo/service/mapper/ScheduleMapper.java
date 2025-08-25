package com.blueseaheart.demo.service.mapper;

import com.blueseaheart.demo.domain.Category;
import com.blueseaheart.demo.domain.Schedule;
import com.blueseaheart.demo.domain.ScheduleStatus;
import com.blueseaheart.demo.domain.Tag;
import com.blueseaheart.demo.domain.User;
import com.blueseaheart.demo.service.dto.CategoryDTO;
import com.blueseaheart.demo.service.dto.ScheduleDTO;
import com.blueseaheart.demo.service.dto.ScheduleStatusDTO;
import com.blueseaheart.demo.service.dto.TagDTO;
import com.blueseaheart.demo.service.dto.UserDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Schedule} and its DTO {@link ScheduleDTO}.
 */
@Mapper(componentModel = "spring")
public interface ScheduleMapper extends EntityMapper<ScheduleDTO, Schedule> {
    @Mapping(target = "owner", source = "owner", qualifiedByName = "userLogin")
    @Mapping(target = "status", source = "status", qualifiedByName = "scheduleStatusName")
    @Mapping(target = "category", source = "category", qualifiedByName = "categoryName")
    @Mapping(target = "tags", source = "tags", qualifiedByName = "tagNameSet")
    ScheduleDTO toDto(Schedule s);

    @Mapping(target = "removeTags", ignore = true)
    Schedule toEntity(ScheduleDTO scheduleDTO);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);

    @Named("scheduleStatusName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ScheduleStatusDTO toDtoScheduleStatusName(ScheduleStatus scheduleStatus);

    @Named("categoryName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    CategoryDTO toDtoCategoryName(Category category);

    @Named("tagName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    TagDTO toDtoTagName(Tag tag);

    @Named("tagNameSet")
    default Set<TagDTO> toDtoTagNameSet(Set<Tag> tag) {
        return tag.stream().map(this::toDtoTagName).collect(Collectors.toSet());
    }
}
