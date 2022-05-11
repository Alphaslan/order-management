package com.jrp.oma.controllers;

import com.jrp.oma.entities.Category;
import com.jrp.oma.services.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryS;

    public CategoryController(CategoryService categoryS) {
        this.categoryS = categoryS;
    }

    //sort
    //pageable

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<Category> findAll() {
        return categoryS.findAll();
    }

    @ResponseStatus(HttpStatus.FOUND)
    @GetMapping("/{id}")
    public Category findById(@PathVariable("id") Long id) {
        if (categoryS.findBy(id).isPresent())
            return categoryS.findBy(id).get();
        return null;
    }

    @ResponseStatus(HttpStatus.FOUND)
    @GetMapping("/name/{name}")
    public Optional<Category> findByName(@PathVariable("name") String name) {
        return categoryS.findBy(name);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/save")
    public Category saveAndFlush(@RequestBody Category category) {
        String temp=category.getName().toLowerCase();
        category.setName(StringUtils.capitalize(temp));
        return categoryS.saveAndFlush(category);
    }

    /**
     * This method checks for duplicate category names and adds only unique values.
     *
     * @param list Will accept category list
     * @return Will return unique added list
     */
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/saveAll")
    public List<Category> saveAll(@RequestBody List<Category> list) {
        HashSet<String> set = new HashSet<>();
        list.removeIf(category -> !set.add(category.getName()));
        return categoryS.saveAll(list);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<Category> updateById(@PathVariable("id") Long id, @RequestBody Category patchCategory) {
        if (categoryS.findBy(id).isPresent())
            return ResponseEntity.badRequest().build();
        Category oldCategory = categoryS.findBy(id).get();

        if (patchCategory.getName() != null)
            oldCategory.setName(patchCategory.getName());

        return ResponseEntity.ok(saveAndFlush(oldCategory));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Category> deleteById(@PathVariable("id") Long id) {
        if (categoryS.findBy(id).isPresent()) {
            categoryS.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.badRequest().build();
    }

}