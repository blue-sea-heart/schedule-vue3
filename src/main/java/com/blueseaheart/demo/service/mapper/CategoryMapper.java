package com.blueseaheart.demo.service.mapper;

import com.blueseaheart.demo.domain.Category;
import com.blueseaheart.demo.service.dto.CategoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Category} and its DTO {@link CategoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface CategoryMapper extends EntityMapper<CategoryDTO, Category> {}
