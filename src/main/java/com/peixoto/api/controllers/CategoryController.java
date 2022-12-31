package com.peixoto.api.controllers;

import com.peixoto.api.dto.NewCategoryDTO;
import com.peixoto.api.entities.Category;
import com.peixoto.api.mapper.CategoryMapper;
import com.peixoto.api.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("category")
public class CategoryController {

    @Autowired
    private final CategoryRepository categoryRepository;

    @GetMapping
    public ResponseEntity<List<Category>> listAllCategories() {
        return ResponseEntity.ok(categoryRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Category>> listCategoryById(@PathVariable long id) {
        return ResponseEntity.ok(categoryRepository.findById(id));
    }

    @PostMapping
    public ResponseEntity<Category> save(@NotNull @Valid @RequestBody NewCategoryDTO category)  throws Exception{
        return new ResponseEntity<>(
                categoryRepository.save(CategoryMapper.INSTANCE.toCategory(category)), HttpStatus.CREATED);
    }

}
