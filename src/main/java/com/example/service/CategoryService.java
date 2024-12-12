package com.example.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.enums.CategoryName;
import com.example.enums.ErrorMessages;
import com.example.model.Category;
import com.example.model.DTOs.CategoryDTO;
import com.example.repository.CategoryRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CategoryService {

	private CategoryRepository categoryRepository;

	public CategoryService(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	public Category getCategoryByName(String category) {
		return categoryRepository.findByCategoryName(CategoryName.valueOf(category.toUpperCase()))
				.orElseThrow(() -> new IllegalArgumentException(
						String.format(ErrorMessages.CATEGORY_NOT_FOUND, category.toString())));

	}

	public List<Category> getAllCategories() {
		return categoryRepository.findAll();
	}

	public List<CategoryDTO> getAllCategoriesAsDTOs() {
		List<Category> allCategories = getAllCategories();

		return allCategories.stream().map(c -> {
			return new CategoryDTO(c.getId(), c.getCategoryName().name());
		}).collect(Collectors.toList());
	}

	public List<CategoryDTO> mapToCategoryDTO(Category category) {

		return Collections.singletonList(new CategoryDTO(category.getId(), category.getCategoryName().name()));
	}

	public Category createNewCategory(String name) {
		CategoryName categoryName = CategoryName.valueOf(name.toUpperCase());
		Category category = new Category();
		category.setCategoryName(CategoryName.valueOf(name.toUpperCase()));
		categoryRepository.save(category);
		return category;
	}

}
