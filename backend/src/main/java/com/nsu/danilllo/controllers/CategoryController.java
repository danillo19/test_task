package com.nsu.danilllo.controllers;


import com.nsu.danilllo.controllers.requests.CategoryRequest;
import com.nsu.danilllo.exceptions.CategoryIsLinkedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.nsu.danilllo.services.impls.CategoryServiceImpl;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityExistsException;
import javax.validation.Valid;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/category")
public class CategoryController {
    private final CategoryServiceImpl categoryService;

    @Autowired
    public CategoryController(CategoryServiceImpl categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createCategory(@Valid @RequestBody CategoryRequest request) {
        try {
            Long id = categoryService.createCategory(request);
            return new ResponseEntity<>(id, HttpStatus.OK);
        } catch (EntityExistsException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,exception.getMessage());
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteCategory(@Valid @RequestParam(required = true) Long id) {
        try {
            categoryService.deleteCategory(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoSuchElementException | CategoryIsLinkedException exception) {
            exception.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,exception.getMessage());
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateCategory(@Valid @RequestBody CategoryRequest request, @RequestParam(required = true) Long id) {
        try {
            Long updatedId = categoryService.updateCategory(request, id);
            return new ResponseEntity<>(updatedId, HttpStatus.OK);
        } catch (NoSuchElementException | EntityExistsException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,exception.getMessage());
        }
    }

    @GetMapping("/get")
    public ResponseEntity<?> getCategoryById(@RequestParam(required = true) Long id) {
        return new ResponseEntity<>(categoryService.getById(id), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<?> findCategoriesByNamePart(@RequestParam(required = false) String name) {
        return new ResponseEntity<>(categoryService.findByNamePart(name), HttpStatus.OK);
    }
}
