package dev.apookash55.finance.controller;

import dev.apookash55.finance.dto.CategoryInfo;
import dev.apookash55.finance.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public void createCategory(@Valid @RequestBody CategoryInfo request, Authentication authentication) {
        String username = authentication.getName();
        categoryService.createCategory(request, username);
    }

    @GetMapping
    public ResponseEntity<List<CategoryInfo>> getCategories(Authentication authentication) {
        String username = authentication.getName();
        List<CategoryInfo> categories = categoryService.getCategories(username);
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryInfo> getCategoryById(@PathVariable Long id, Authentication authentication) {
        String username = authentication.getName();
        CategoryInfo category = categoryService.getCategory(id, username);
        return ResponseEntity.ok(category);
    }

    @PutMapping("/{id}")
    public void updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryInfo request, Authentication authentication) {
        String username = authentication.getName();
        categoryService.updateCategory(request, username, id);
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id, Authentication authentication) {
        String username = authentication.getName();
        categoryService.deleteCategory(id, username);
    }
}
