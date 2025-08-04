package com.sicua.infrastructure.persistence.category;

import com.sicua.domain.category.entity.Category;
import com.sicua.domain.category.entity.CategoryId;
import com.sicua.domain.category.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@Transactional
public class CategoryRepositoryImpl implements CategoryRepository {

    private final JpaCategoryRepository jpaCategoryRepository;

    @Autowired
    public CategoryRepositoryImpl(JpaCategoryRepository jpaCategoryRepository) {
        this.jpaCategoryRepository = jpaCategoryRepository;
    }

    @Override
    public Category save(Category category) {
        CategoryEntity entity = toEntity(category);
        CategoryEntity savedEntity = jpaCategoryRepository.save(entity);
        return toDomain(savedEntity);
    }

    @Override
    public Optional<Category> findByIdAndStoreId(CategoryId categoryId, String storeId) {
        return jpaCategoryRepository.findByCategoryIdAndStoreId(categoryId.getValue(), storeId)
                .map(this::toDomain);
    }

    @Override
    public List<Category> findByStoreId(String storeId) {
        return jpaCategoryRepository.findByStoreIdOrderByCategoryNumber(storeId)
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Category> findByCategoryNumberAndStoreId(Integer categoryNumber, String storeId) {
        return jpaCategoryRepository.findByCategoryNumberAndStoreId(categoryNumber, storeId)
                .map(this::toDomain);
    }

    @Override
    public Optional<Category> findByNameAndStoreId(String name, String storeId) {
        return jpaCategoryRepository.findByNameAndStoreId(name, storeId)
                .map(this::toDomain);
    }

    @Override
    public void deleteByIdAndStoreId(CategoryId categoryId, String storeId) {
        jpaCategoryRepository.deleteByCategoryIdAndStoreId(categoryId.getValue(), storeId);
    }

    @Override
    public boolean existsByIdAndStoreId(CategoryId categoryId, String storeId) {
        return jpaCategoryRepository.existsByCategoryIdAndStoreId(categoryId.getValue(), storeId);
    }

    @Override
    public boolean existsByCategoryNumberAndStoreIdAndIdNot(Integer categoryNumber, String storeId, CategoryId excludeCategoryId) {
        return jpaCategoryRepository.existsByCategoryNumberAndStoreIdAndCategoryIdNot(
                categoryNumber, storeId, excludeCategoryId.getValue());
    }

    @Override
    public boolean existsByNameAndStoreIdAndIdNot(String name, String storeId, CategoryId excludeCategoryId) {
        return jpaCategoryRepository.existsByNameAndStoreIdAndCategoryIdNot(
                name, storeId, excludeCategoryId.getValue());
    }

    @Override
    public Integer getNextCategoryNumber(String storeId) {
        return jpaCategoryRepository.findNextCategoryNumber(storeId);
    }

    private CategoryEntity toEntity(Category category) {
        return new CategoryEntity(
                category.getCategoryId().getValue(),
                category.getStoreId(),
                category.getName(),
                category.getCategoryNumber(),
                category.getCreatedAt(),
                category.getUpdatedAt()
        );
    }

    private Category toDomain(CategoryEntity entity) {
        return new Category(
                CategoryId.of(entity.getCategoryId()),
                entity.getStoreId(),
                entity.getName(),
                entity.getCategoryNumber(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
