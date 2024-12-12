package com.example.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.enums.CategoryName;
import com.example.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

	Optional<Category> findByCategoryName(CategoryName name);
}
