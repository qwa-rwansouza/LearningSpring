package com.example.algamoney.api.resource;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.algamoney.api.model.Category;
import com.example.algamoney.api.repository.CategoryRepository;

@RestController
@RequestMapping("/category")
public class CategoryResource {
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@GetMapping
	public ResponseEntity<?> findAll() {
		List<Category> category = categoryRepository.findAll(); 
		return !category.isEmpty() ? ResponseEntity.ok(category) : ResponseEntity.notFound().build(); 
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Category> create(@Valid @RequestBody Category category, HttpServletResponse response) {
		Category categorySave = categoryRepository.save(category);
		
		URI uri =  ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}")
				.buildAndExpand(categorySave.getId()).toUri();
		response.setHeader("Location", uri.toASCIIString());
		
		return ResponseEntity.created(uri).body(categorySave);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Category> getById(@PathVariable Long id) {
		Optional<Category> category  = categoryRepository.findById(id);
		return category.isPresent() ?
				ResponseEntity.ok(category.get()) : ResponseEntity.notFound().build();
	}	
}
