package io.github.pauliustechin.egzaminas.feature.category.service;

import io.github.pauliustechin.egzaminas.feature.category.dto.CategoryListResponse;
import io.github.pauliustechin.egzaminas.feature.category.dto.CategoryResponse;
import io.github.pauliustechin.egzaminas.feature.category.dto.CreateCategoryRequest;

public interface CategoryService {
    CategoryResponse createCategory(CreateCategoryRequest request);

    void deleteCategory(Long categoryId);

    CategoryListResponse getAllCategories();
}
