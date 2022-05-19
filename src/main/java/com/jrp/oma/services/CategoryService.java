package com.jrp.oma.services;

import com.jrp.oma.dao.CategoryRepository;
import com.jrp.oma.entities.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    CategoryRepository categoryR;

    public Optional<Category> findBy(String name) {
        return categoryR.findByName(name);
    }

    public Optional<Category> findBy(Long id) {
        return categoryR.findById(id);
    }

    public List<Category> findAll() {
        return categoryR.findAll();
    }

    public Iterable<Category> findAll(Pageable pageable) {
        return categoryR.findAll(pageable);
    }


    public void deleteById(Long id) {
        categoryR.deleteById(id);
    }

    public List<Category> saveAll(Iterable<Category> iterable) {
        return categoryR.saveAll(iterable);
    }

    public Category saveAndFlush(Category category) {
        return categoryR.saveAndFlush(category);
    }


}
