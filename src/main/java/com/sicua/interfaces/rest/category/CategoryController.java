package com.sicua.interfaces.rest.category;

import com.sicua.application.category.dto.CategoryResponse;
import com.sicua.application.category.dto.CreateCategoryRequest;
import com.sicua.application.category.dto.UpdateCategoryRequest;
import com.sicua.application.category.usecase.CategoryService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(
            @RequestBody @Valid CreateCategoryRequest request,
            HttpSession session) {
        
        String storeId = (String) session.getAttribute("storeId");
        if (storeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            CategoryResponse response = categoryService.createCategory(storeId, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories(HttpSession session) {
        String storeId = (String) session.getAttribute("storeId");
        if (storeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<CategoryResponse> categories = categoryService.getAllCategories(storeId);
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> getCategoryById(
            @PathVariable String categoryId,
            HttpSession session) {
        
        String storeId = (String) session.getAttribute("storeId");
        if (storeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Optional<CategoryResponse> category = categoryService.getCategoryById(storeId, categoryId);
        return category.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/number/{categoryNumber}")
    public ResponseEntity<CategoryResponse> getCategoryByNumber(
            @PathVariable Integer categoryNumber,
            HttpSession session) {
        
        String storeId = (String) session.getAttribute("storeId");
        if (storeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Optional<CategoryResponse> category = categoryService.getCategoryByNumber(storeId, categoryNumber);
        return category.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> updateCategory(
            @PathVariable String categoryId,
            @RequestBody @Valid UpdateCategoryRequest request,
            HttpSession session) {
        
        String storeId = (String) session.getAttribute("storeId");
        if (storeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            CategoryResponse response = categoryService.updateCategory(storeId, categoryId, request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategory(
            @PathVariable String categoryId,
            HttpSession session) {
        
        String storeId = (String) session.getAttribute("storeId");
        if (storeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            categoryService.deleteCategory(storeId, categoryId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/next-number")
    public ResponseEntity<Integer> getNextCategoryNumber(HttpSession session) {
        String storeId = (String) session.getAttribute("storeId");
        if (storeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Integer nextNumber = categoryService.getNextCategoryNumber(storeId);
        return ResponseEntity.ok(nextNumber);
    }
}
