package com.peixoto.api.mapper;

import com.peixoto.api.dto.NewBook;
import com.peixoto.api.entities.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel="spring")
public abstract class BookMapper {
    public static final BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

    @Mapping(target = "id", ignore = true)
    public abstract Book toBook(NewBook newBook);
}
