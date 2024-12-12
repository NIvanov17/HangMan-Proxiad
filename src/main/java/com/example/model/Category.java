package com.example.model;

import java.util.ArrayList;
import java.util.List;

import com.example.enums.CategoryName;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "category")
public class Category {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(nullable =  false)
	@Enumerated(EnumType.STRING)
	private CategoryName categoryName;
	
	@OneToMany(mappedBy = "category")
	private List<Word> words;
	
	public Category() {
		words = new ArrayList<>();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public CategoryName getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(CategoryName categoryName) {
		this.categoryName = categoryName;
	}

	public List<Word> getWords() {
		return words;
	}

	public void setWords(List<Word> words) {
		this.words = words;
	}

}
