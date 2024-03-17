package org.restro.service;

import org.restro.entity.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    List<Category> findAllCategory();

    void saveCategory(Category category);

    void deleteCategoryById(int id);

    void updateCategory(Category category);

    Optional<Category> findById(int id);
}
