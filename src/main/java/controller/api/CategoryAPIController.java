package controller.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import model.Category;
import model.DTOs.CategoryDTO;
import service.CategoryService;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Category API Controller")
@CrossOrigin(origins = "http://localhost:3000")
public class CategoryAPIController {

	private final CategoryService categoryService;

	@Autowired
	public CategoryAPIController(CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	@GetMapping("/categories")
	@Operation(summary = "Get categories or get all catgories")
	public ResponseEntity<List<CategoryDTO>> getCategoryByName(@RequestParam(required = false) String categoryName ) {
		
		if(categoryName != null) {
			Category categoryByName = categoryService.getCategoryByName(categoryName);
			return ResponseEntity.ok(categoryService.mapToCategoryDTO(categoryByName));
		}

		return ResponseEntity.ok(categoryService.getAllCategoriesAsDTOs());
	}

}
