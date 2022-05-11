package com.jrp.oma.dao;

import com.jrp.oma.entities.Product;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByName(String name);

    List<Product> findByCategoryId(long id);

    List<Product> findByOrderListId(long id);

    List<Product> findByPriceBetween(BigDecimal min, BigDecimal max);
}
