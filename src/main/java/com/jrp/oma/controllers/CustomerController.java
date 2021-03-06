package com.jrp.oma.controllers;

import com.jrp.oma.entities.Customer;
import com.jrp.oma.services.CustomerService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerS;

    public CustomerController(CustomerService customerS) {
        this.customerS = customerS;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public Iterable<Customer> findAll() {
        return customerS.findAll();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/pageable")
    public Iterable<Customer> findAllPaginated(@RequestParam(value = "page", defaultValue = "0") int page,
                                      @RequestParam(value = "size", defaultValue = "50") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return customerS.findAll(pageable);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/lname-sorted")
    public Iterable<Customer> findAllSortedLnameAndPaginated(@RequestParam(value = "page", defaultValue = "0") int page,
                                               @RequestParam(value = "size", defaultValue = "50") int size) {
        Sort lnameSort = Sort.by("lname");
        Pageable pageable = PageRequest.of(page, size).withSort(lnameSort);
        return customerS.findAll(pageable);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/order-sorted")
    public Iterable<Customer> findAllSortedOrderNumAndPaginated(@RequestParam(value = "page", defaultValue = "0") int page,
                                                             @RequestParam(value = "size", defaultValue = "50") int size) {
        Sort orderSort = Sort.by("orderList").descending().and(Sort.by("lname"));
        Pageable pageable = PageRequest.of(page, size).withSort(orderSort);
        return customerS.findAll(pageable);
    }

    @ResponseStatus(HttpStatus.FOUND)
    @GetMapping("/{id}")
    public Optional<Customer> findById(@PathVariable("id") Long id) {
        return customerS.findBy(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/email/{email}")
    public List<Customer> findByEmailContains(@PathVariable String email) {
        return customerS.findByEmailContains(email);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/firstname/{fname}")
    public List<Customer> findAllByFirstName(@PathVariable("fname") String fname) {
        return customerS.findAllByFname(fname);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/lastName/{lname}")
    public List<Customer> findAllByLastName(@PathVariable("lname") String lname) {
        return customerS.findAllByLname(lname);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/save")
    public Customer saveAndFlush(@RequestBody Customer customer) {
        String fname = customer.getFname();
        customer.setFname(fname.substring(0, 1).toUpperCase() + fname.substring(1).toLowerCase());

        customer.setLname(customer.getLname().toUpperCase());
        return customerS.saveAndFlush(customer);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/save-all")
    public List<Customer> saveAll(@RequestBody List<Customer> list) {
        List<Customer> addedList = new ArrayList<>();
        for (Customer customer : list) {
            if (saveAndFlush(customer) != null)
                addedList.add(customer);
        }
        return addedList;
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<Customer> updateById(@PathVariable("id") long id, @RequestBody Customer customer) {
        if (!customerS.findBy(id).isPresent())
            return ResponseEntity.badRequest().build();

        Customer oldCustomer = customerS.findBy(id).get();

        if (customer.getFname() != null)
            oldCustomer.setFname(customer.getFname());

        if (customer.getLname() != null)
            oldCustomer.setLname(customer.getLname());

        if (customer.getEmail() != null)
            oldCustomer.setEmail(customer.getEmail());

        return ResponseEntity.ok(saveAndFlush(oldCustomer));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Customer> deleteById(@PathVariable("id") Long id) {
        if (customerS.findBy(id).isPresent()) {
            customerS.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.badRequest().build();
    }

}
