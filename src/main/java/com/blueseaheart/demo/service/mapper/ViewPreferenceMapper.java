package com.blueseaheart.demo.service.mapper;

import com.blueseaheart.demo.domain.User;
import com.blueseaheart.demo.domain.ViewPreference;
import com.blueseaheart.demo.service.dto.UserDTO;
import com.blueseaheart.demo.service.dto.ViewPreferenceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ViewPreference} and its DTO {@link ViewPreferenceDTO}.
 */
@Mapper(componentModel = "spring")
public interface ViewPreferenceMapper extends EntityMapper<ViewPreferenceDTO, ViewPreference> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    ViewPreferenceDTO toDto(ViewPreference s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}
