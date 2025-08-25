package com.blueseaheart.demo.service.mapper;

import com.blueseaheart.demo.domain.InAppNotification;
import com.blueseaheart.demo.domain.User;
import com.blueseaheart.demo.service.dto.InAppNotificationDTO;
import com.blueseaheart.demo.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link InAppNotification} and its DTO {@link InAppNotificationDTO}.
 */
@Mapper(componentModel = "spring")
public interface InAppNotificationMapper extends EntityMapper<InAppNotificationDTO, InAppNotification> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    InAppNotificationDTO toDto(InAppNotification s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}
