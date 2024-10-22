package service;

import org.springframework.stereotype.Service;

import enums.CategoryName;
import model.Category;
import repository.CategoryRepository;

@Service
public class CategoryService {

	private CategoryRepository categoryRepository;

	public CategoryService(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	public Category getCategoryByName(String category) {
		return categoryRepository.findByCategoryName(CategoryName.valueOf(category.toUpperCase()))
				.orElseThrow(() -> new IllegalArgumentException("Ooopss... Category is not existing."));

	}

}
