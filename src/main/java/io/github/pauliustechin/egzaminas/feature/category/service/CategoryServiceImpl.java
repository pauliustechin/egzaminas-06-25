package io.github.pauliustechin.egzaminas.feature.category.service;

import io.github.pauliustechin.egzaminas.exception.ResourceNotFoundException;
import io.github.pauliustechin.egzaminas.feature.category.dto.CategoryListResponse;
import io.github.pauliustechin.egzaminas.feature.category.dto.CategoryMapper;
import io.github.pauliustechin.egzaminas.feature.category.dto.CategoryResponse;
import io.github.pauliustechin.egzaminas.feature.category.dto.CreateCategoryRequest;
import io.github.pauliustechin.egzaminas.feature.category.model.Category;
import io.github.pauliustechin.egzaminas.feature.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryResponse createCategory(CreateCategoryRequest request) {

        Category category = categoryMapper.toEntity(request);
        Category savedCategory = categoryRepository.save(category);

        return categoryMapper.toResponse(savedCategory);
    }

    @Override
    public void deleteCategory(Long categoryId) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", categoryId));

        categoryRepository.delete(category);

    }

    @Override
    public CategoryListResponse getAllCategories() {

        List<Category> categories = categoryRepository.findAll();

        List<CategoryResponse> categoryResponses = categories.stream()
                .map(category -> categoryMapper.toResponse(category))
                .toList();

        return new CategoryListResponse(categoryResponses);
    }
}
