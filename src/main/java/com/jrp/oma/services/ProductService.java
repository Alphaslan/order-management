package com.jrp.oma.services;

import com.jrp.oma.dao.ProductRepository;
import com.jrp.oma.entities.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    ProductRepository productR;

    public List<Product> findAll() {
        return productR.findAll();
    }

    public Iterable<Product> findAll(Pageable pageable) {
        return productR.findAll(pageable);
    }

    public Optional<Product> findBy(Long id) {
        return productR.findById(id);
    }

    public Optional<Product> findBy(String name) {
        return productR.findByName(name);
    }

    public List<Product> findByOrderId(Long id) {
        return productR.findByOrderListId(id);
    }

    public List<Product> findByPriceBetween(BigDecimal min, BigDecimal max) {
        return productR.findByPriceBetween(min, max);
    }

    public List<Product> findAllByCategory(Long categoryId) {
        return productR.findByCategoryId(categoryId);
    }

    public void deleteById(Long id) {
        productR.deleteById(id);
    }

    public Product saveAndFlush(Product product) {
        return productR.saveAndFlush(product);
    }
}
