package com.peixoto.api.mapper;

import com.peixoto.api.dto.NewCategoryDTO;
import com.peixoto.api.entities.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel="spring")
public abstract class CategoryMapper {
    public static final CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    @Mapping(target = "id", ignore = true)
    public abstract Category toCategory(NewCategoryDTO category);
}
