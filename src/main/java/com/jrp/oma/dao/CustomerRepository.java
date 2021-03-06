package com.jrp.oma.dao;

import com.jrp.oma.entities.Customer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long> {
    List<Customer> findAllByFname(String name);

    List<Customer> findAllByLname(String name);

    List<Customer> findByEmailContains (String email);
}
