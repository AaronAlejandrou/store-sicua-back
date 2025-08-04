package com.sicua.application.category.usecase;

import com.sicua.application.category.dto.CategoryResponse;
import com.sicua.application.category.dto.CreateCategoryRequest;
import com.sicua.application.category.dto.UpdateCategoryRequest;
import com.sicua.domain.category.entity.Category;
import com.sicua.domain.category.entity.CategoryId;
import com.sicua.domain.category.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CategoryResponse createCategory(String storeId, CreateCategoryRequest request) {
        // Check if category number already exists for this store
        if (categoryRepository.findByCategoryNumberAndStoreId(request.getCategoryNumber(), storeId).isPresent()) {
            throw new IllegalArgumentException("Category number " + request.getCategoryNumber() + " already exists for this store");
        }

        // Check if category name already exists for this store
        if (categoryRepository.findByNameAndStoreId(request.getName(), storeId).isPresent()) {
            throw new IllegalArgumentException("Category name '" + request.getName() + "' already exists for this store");
        }

        Category category = new Category(
                CategoryId.generate(),
                storeId,
                request.getName(),
                request.getCategoryNumber()
        );

        Category savedCategory = categoryRepository.save(category);
        return toResponse(savedCategory);
    }

    public CategoryResponse updateCategory(String storeId, String categoryId, UpdateCategoryRequest request) {
        CategoryId id = CategoryId.of(categoryId);
        
        Optional<Category> categoryOpt = categoryRepository.findByIdAndStoreId(id, storeId);
        if (categoryOpt.isEmpty()) {
            throw new IllegalArgumentException("Category not found");
        }

        Category category = categoryOpt.get();

        // Check if new category number conflicts with existing categories (excluding current one)
        if (categoryRepository.existsByCategoryNumberAndStoreIdAndIdNot(request.getCategoryNumber(), storeId, id)) {
            throw new IllegalArgumentException("Category number " + request.getCategoryNumber() + " already exists for this store");
        }

        // Check if new category name conflicts with existing categories (excluding current one)
        if (categoryRepository.existsByNameAndStoreIdAndIdNot(request.getName(), storeId, id)) {
            throw new IllegalArgumentException("Category name '" + request.getName() + "' already exists for this store");
        }

        category.updateCategory(request.getName(), request.getCategoryNumber());
        Category updatedCategory = categoryRepository.save(category);
        return toResponse(updatedCategory);
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategories(String storeId) {
        return categoryRepository.findByStoreId(storeId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<CategoryResponse> getCategoryById(String storeId, String categoryId) {
        return categoryRepository.findByIdAndStoreId(CategoryId.of(categoryId), storeId)
                .map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public Optional<CategoryResponse> getCategoryByNumber(String storeId, Integer categoryNumber) {
        return categoryRepository.findByCategoryNumberAndStoreId(categoryNumber, storeId)
                .map(this::toResponse);
    }

    public void deleteCategory(String storeId, String categoryId) {
        CategoryId id = CategoryId.of(categoryId);
        
        if (!categoryRepository.existsByIdAndStoreId(id, storeId)) {
            throw new IllegalArgumentException("Category not found");
        }

        // TODO: Check if category is being used by products before deletion
        // This would require ProductRepository to have a method like existsByCategoryNumberAndStoreId
        
        categoryRepository.deleteByIdAndStoreId(id, storeId);
    }

    @Transactional(readOnly = true)
    public Integer getNextCategoryNumber(String storeId) {
        return categoryRepository.getNextCategoryNumber(storeId);
    }

    private CategoryResponse toResponse(Category category) {
        return new CategoryResponse(
                category.getCategoryId().getValue(),
                category.getName(),
                category.getCategoryNumber(),
                category.getCreatedAt(),
                category.getUpdatedAt()
        );
    }
}
