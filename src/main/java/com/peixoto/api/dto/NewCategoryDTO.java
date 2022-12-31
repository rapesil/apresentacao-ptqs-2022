package com.peixoto.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewCategoryDTO {

    @NotNull(message = "Category name cannot be null")
    @NotEmpty(message = "Category name cannot be null")
    private String name;
}
