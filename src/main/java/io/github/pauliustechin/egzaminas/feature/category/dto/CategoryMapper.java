package io.github.pauliustechin.egzaminas.feature.category.dto;

import io.github.pauliustechin.egzaminas.feature.category.model.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public Category toEntity(CreateCategoryRequest request) {
        Category category = new Category();

        category.setCategoryName(request.categoryName());
        category.setDescription(request.description());

        return category;
    }


    public CategoryResponse toResponse(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getCategoryName(),
                category.getDescription()
        );
    }
}