package com.jrp.oma.services;

import com.jrp.oma.dao.CustomerRepository;
import com.jrp.oma.entities.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {
    @Autowired
    CustomerRepository customerR;

    public List<Customer> findAllByFname(String name) {
        return customerR.findAllByFname(name);
    }

    public List<Customer> findAllByLname(String name) {
        return customerR.findAllByLname(name);
    }

    public List<Customer> findAll() {
        return customerR.findAll();
    }

    public Iterable<Customer> findAll(Pageable pageable) {
        return customerR.findAll(pageable);
    }

    public Optional<Customer> findBy(Long id) {
        return customerR.findById(id);
    }

    public List<Customer> findByEmailContains (String email) {
        return customerR.findByEmailContains(email);
    }

    public Customer saveAndFlush(Customer customer) {
        return customerR.saveAndFlush(customer);
    }

    public void deleteById(Long id) {
        customerR.deleteById(id);
    }

}
