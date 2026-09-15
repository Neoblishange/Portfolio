package com.neoblishange.portfolio.service;

import com.neoblishange.portfolio.dto.category.CategoryRequestDTO;
import com.neoblishange.portfolio.dto.category.CategoryResponseDTO;
import com.neoblishange.portfolio.entity.Category;
import com.neoblishange.portfolio.exception.ResourceNotFoundException;
import com.neoblishange.portfolio.mapper.CategoryMapper;
import com.neoblishange.portfolio.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void shouldReturnCategoryWhenCategoryExists() {

        // Arrange
        Long id = 1L;

        Category category = new Category();
        category.setId(id);
        category.setName("Backend");

        CategoryResponseDTO response = new CategoryResponseDTO(
                id,
                "Backend"
        );

        when(categoryRepository.findById(id))
                .thenReturn(Optional.of(category));

        when(categoryMapper.toResponse(category))
                .thenReturn(response);

        // Act
        CategoryResponseDTO result =
                categoryService.getCategoryById(id);

        // Assert
        assertNotNull(result);
        assertEquals(response, result);

        verify(categoryRepository).findById(id);
        verify(categoryMapper).toResponse(category);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenCategoryDoesNotExist() {

        // Arrange
        Long id = 999L;

        when(categoryRepository.findById(id))
                .thenReturn(Optional.empty());

        // Act + Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> categoryService.getCategoryById(id)
        );

        assertEquals(
                "Category with id " + id + " not found",
                exception.getMessage()
        );

        verify(categoryRepository).findById(id);
        verify(categoryMapper, never()).toResponse(any());
    }

    @Test
    void shouldReturnAllCategories() {

        // Arrange
        Category category1 = new Category();
        category1.setId(1L);
        category1.setName("Backend");

        Category category2 = new Category();
        category2.setId(2L);
        category2.setName("Frontend");

        CategoryResponseDTO response1 = new CategoryResponseDTO(
                1L,
                "Backend"
        );

        CategoryResponseDTO response2 = new CategoryResponseDTO(
                2L,
                "Frontend"
        );

        when(categoryRepository.findAll())
                .thenReturn(List.of(category1, category2));

        when(categoryMapper.toResponse(category1))
                .thenReturn(response1);

        when(categoryMapper.toResponse(category2))
                .thenReturn(response2);

        // Act
        List<CategoryResponseDTO> result =
                categoryService.getAllCategories();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());

        assertEquals(response1, result.get(0));
        assertEquals(response2, result.get(1));

        verify(categoryRepository).findAll();
        verify(categoryMapper).toResponse(category1);
        verify(categoryMapper).toResponse(category2);
    }

    @Test
    void shouldReturnEmptyListWhenNoCategoryExists() {

        // Arrange
        when(categoryRepository.findAll())
                .thenReturn(List.of());

        // Act
        List<CategoryResponseDTO> result =
                categoryService.getAllCategories();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(categoryRepository).findAll();
        verifyNoInteractions(categoryMapper);
    }

    @Test
    void shouldCreateCategorySuccessfully() {

        // Arrange
        CategoryRequestDTO request = new CategoryRequestDTO("Backend");

        Category category = new Category();

        Category savedCategory = new Category();
        savedCategory.setId(1L);
        savedCategory.setName("Backend");

        CategoryResponseDTO response = new CategoryResponseDTO(
                1L,
                "Backend"
        );

        when(categoryMapper.toEntity(request))
                .thenReturn(category);

        when(categoryRepository.save(category))
                .thenReturn(savedCategory);

        when(categoryMapper.toResponse(savedCategory))
                .thenReturn(response);

        // Act
        CategoryResponseDTO result =
                categoryService.createCategory(request);

        // Assert
        assertNotNull(result);
        assertEquals(response, result);

        verify(categoryMapper).toEntity(request);
        verify(categoryRepository).save(category);
        verify(categoryMapper).toResponse(savedCategory);
    }

    @Test
    void shouldUpdateCategorySuccessfully() {

        // Arrange
        Long id = 1L;

        CategoryRequestDTO request = new CategoryRequestDTO("Backend Updated");

        Category category = new Category();
        category.setId(id);
        category.setName("Backend");

        CategoryResponseDTO response = new CategoryResponseDTO(
                id,
                "Backend Updated"
        );

        when(categoryRepository.findById(id))
                .thenReturn(Optional.of(category));

        when(categoryMapper.toResponse(category))
                .thenReturn(response);

        // Act
        CategoryResponseDTO result =
                categoryService.updateCategory(request, id);

        // Assert
        assertNotNull(result);
        assertEquals(response, result);

        verify(categoryRepository).findById(id);
        verify(categoryMapper).updateEntity(request, category);
        verify(categoryMapper).toResponse(category);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenUpdatingNonExistingCategory() {

        // Arrange
        Long id = 999L;

        CategoryRequestDTO request = new CategoryRequestDTO("Backend");

        when(categoryRepository.findById(id))
                .thenReturn(Optional.empty());

        // Act + Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> categoryService.updateCategory(request, id)
        );

        assertEquals(
                "Category with id " + id + " not found",
                exception.getMessage()
        );

        verify(categoryRepository).findById(id);
        verify(categoryMapper, never()).updateEntity(any(), any());
        verify(categoryMapper, never()).toResponse(any());
    }

    @Test
    void shouldDeleteCategorySuccessfully() {

        // Arrange
        Long id = 1L;

        // Act
        categoryService.deleteCategory(id);

        // Assert
        verify(categoryRepository).deleteById(id);
    }
}