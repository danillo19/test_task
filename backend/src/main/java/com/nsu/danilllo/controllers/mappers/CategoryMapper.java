package com.nsu.danilllo.controllers.mappers;

import com.nsu.danilllo.controllers.dto.CategoryDto;
import com.nsu.danilllo.model.Category;

public class CategoryMapper {
    public static CategoryDto toDto(Category category) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName(category.getName());
        categoryDto.setId(category.getId());
        return categoryDto;
    }
}
