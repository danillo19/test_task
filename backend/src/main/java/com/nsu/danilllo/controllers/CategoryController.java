package com.nsu.danilllo.controllers;


import com.nsu.danilllo.controllers.requests.CategoryRequest;
import com.nsu.danilllo.exceptions.CategoryIsLinkedException;
import com.nsu.danilllo.exceptions.NoEntityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.nsu.danilllo.services.impls.CategoryServiceImpl;

import javax.persistence.EntityExistsException;
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
    public ResponseEntity<?> createCategory(@RequestBody CategoryRequest request) {
        try {
            Long id = categoryService.createCategory(request);
            return new ResponseEntity<>(id, HttpStatus.OK);
        } catch (EntityExistsException exception) {
            //TODO:add logs.
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteCategory(@RequestParam(required = true) Long id) {
        try {
            categoryService.deleteCategory(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoEntityException | CategoryIsLinkedException exception) {
            exception.printStackTrace();
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateCategory(@RequestBody CategoryRequest request, @RequestParam(required = true) Long id) {
        try {
            Long updatedId = categoryService.updateCategory(request, id);
            return new ResponseEntity<>(updatedId, HttpStatus.OK);
        } catch (NoEntityException | EntityExistsException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/get")
    public ResponseEntity<?> getCategoryById(@RequestParam(required = true) Long id) {
        try {
            return new ResponseEntity<>(categoryService.getById(id), HttpStatus.OK);
        } catch (NoSuchElementException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> findCategoriesByNamePart(@RequestParam(required = false) String name) {
        return new ResponseEntity<>(categoryService.findByNamePart(name), HttpStatus.OK);
    }
}
