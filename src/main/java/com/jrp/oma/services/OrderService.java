package com.jrp.oma.services;

import com.jrp.oma.dao.OrderRepository;
import com.jrp.oma.entities.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    OrderRepository orderR;

    public Optional<Order> findBy(long id) {
        return orderR.findById(id);
    }

    public List<Order> findByActiveStatus(boolean isActive) {
        return orderR.findByActiveStatus(isActive);
    }

    public List<Order> findByCustomer(long customerId) {
        return orderR.findByCustomerId(customerId);
    }

    public List<Order> findByCustomerFname(String fname) {
        return orderR.findByCustomerFname(fname);
    }

    public List<Order> findByCustomerLname(String lname) {
        return orderR.findByCustomerLname(lname);
    }

    public List<Order> findByAddress(long addressId) {
        return orderR.findByAddressId(addressId);
    }

    public List<Order> findAll() {
        return orderR.findAll();
    }

    public List<Order> findByCreationDateBefore (LocalDateTime dateTime){
        return orderR.findByCreationDateBefore(dateTime);
    }

    public List<Order> findByCreationDateAfter (LocalDateTime dateTime){
        return orderR.findByCreationDateAfter(dateTime);
    }

    public List<Order> findByCreationDateIsBetween (LocalDateTime startTime,LocalDateTime endTime){
        return orderR.findByCreationDateIsBetween(startTime,endTime);
    }

    public List<Order> saveAll(Iterable<Order> iterable) {
        return orderR.saveAll(iterable);
    }

    public Order saveAndFlush(Order order) {
        return orderR.saveAndFlush(order);
    }

    public void deleteById(Long id) {
        orderR.deleteById(id);
    }


}
