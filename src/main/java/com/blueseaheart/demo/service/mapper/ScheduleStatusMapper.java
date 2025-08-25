package com.blueseaheart.demo.service.mapper;

import com.blueseaheart.demo.domain.ScheduleStatus;
import com.blueseaheart.demo.service.dto.ScheduleStatusDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ScheduleStatus} and its DTO {@link ScheduleStatusDTO}.
 */
@Mapper(componentModel = "spring")
public interface ScheduleStatusMapper extends EntityMapper<ScheduleStatusDTO, ScheduleStatus> {}
