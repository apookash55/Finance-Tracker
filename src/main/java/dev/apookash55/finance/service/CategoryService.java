package dev.apookash55.finance.service;

import dev.apookash55.finance.dto.CategoryInfo;
import dev.apookash55.finance.dto.CategoryType;
import dev.apookash55.finance.entity.Category;
import dev.apookash55.finance.entity.User;
import dev.apookash55.finance.repository.CategoryRepository;
import dev.apookash55.finance.repository.CredentialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CredentialRepository credentialRepository;

    @Transactional
    public void createCategory(CategoryInfo request, String username) {
        User user = getUser(username);

        Category category = new Category();
        category.setUser(user);
        category.setName(request.getName());
        category.setType(request.getType().name());
        categoryRepository.save(category);
    }

    @Transactional(readOnly = true)
    public List<CategoryInfo> getCategories(String username) {
        User user = getUser(username);
        List<Category> categories = categoryRepository.findByUser(user);
        return categories.stream().map(category -> new CategoryInfo(category.getId(), category.getName(), CategoryType.valueOf(category.getType()))).toList();
    }

    @Transactional(readOnly = true)
    public CategoryInfo getCategory(Long id, String username) {
        Category category = verifyCategory(id, username);
        return new CategoryInfo(category.getId(), category.getName(), CategoryType.valueOf(category.getType()));
    }

    @Transactional
    public void updateCategory(CategoryInfo request, String username, Long id) {
        Category category = verifyCategory(id, username);
        category.setName(request.getName());
        category.setType(request.getType().name());
        categoryRepository.save(category);
    }

    @Transactional
    public void deleteCategory(Long id, String username) {
        Category category = verifyCategory(id, username);
        categoryRepository.delete(category);
    }

    private User getUser(String username) {
        return credentialRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Invalid username")).getUser();
    }

    private Category verifyCategory(Long categoryId, String username) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new IllegalArgumentException("Invalid category"));
        User user = credentialRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Invalid username")).getUser();
        if (!category.getUser().equals(user)) {
            throw new IllegalArgumentException("Invalid category-user mapping");
        }
        return category;
    }
}
