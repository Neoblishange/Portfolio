package com.neoblishange.portfolio.controller;

import com.neoblishange.portfolio.dto.category.CategoryRequestDTO;
import com.neoblishange.portfolio.dto.category.CategoryResponseDTO;
import com.neoblishange.portfolio.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Categories",
        description = "Manage categories of skills"
)
@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(
            summary = "Get all categories"
    )
    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @Operation(
            summary = "Get one category by id"
    )
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @Operation(
            summary = "Create a category"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Category successfully created"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid category data"
            ),
    })
    @PostMapping
    public ResponseEntity<CategoryResponseDTO> createCategory(@RequestBody CategoryRequestDTO categoryRequest) {
        CategoryResponseDTO response = categoryService.createCategory(categoryRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Update a category"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Category successfully updated"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid category data"
            ),
    })
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> updateCategory(
            @RequestBody CategoryRequestDTO categoryRequest,
            @PathVariable Long id) {
        return ResponseEntity.ok(categoryService.updateCategory(categoryRequest, id));
    }

    @Operation(
            summary = "Delete a category"
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
    }
}
