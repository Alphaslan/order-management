package com.jrp.oma.controllers;

import com.jrp.oma.entities.Order;
import com.jrp.oma.services.AddressService;
import com.jrp.oma.services.CustomerService;
import com.jrp.oma.services.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderS;
    private final CustomerService customerS;
    private final AddressService addressS;
    private boolean active;

    public OrderController(OrderService orderS, CustomerService customerS, AddressService addressS) {
        this.orderS = orderS;
        this.customerS = customerS;
        this.addressS = addressS;
    }

    // find by *all *id *activeStatus creationDate *before *after *between *customerId *addressId
    //*activate *dis-activate
    //add *one *all
    //update *patch
    //delete *one
    //unique
    //sort
    //pageable

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/")
    public List<Order> findAll() {
        return orderS.findAll();
    }

    @ResponseStatus(HttpStatus.FOUND)
    @GetMapping("/{id}")
    public Optional<Order> findById(@PathVariable("id") Long id) {
        return orderS.findBy(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/activeStatus/{isActive}")
    public List<Order> findByActiveStatus(@PathVariable String isActive) {
        boolean b = isActive.equalsIgnoreCase("active");
        return orderS.findByActiveStatus(b);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/customer/{id}")
    public List<Order> findByCustomerId(@PathVariable long id) {
        return orderS.findByCustomer(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/address/{id}")
    public List<Order> findByAddressId(@PathVariable long id) {
        return orderS.findByAddress(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/before")
    public List<Order> createdBefore(@RequestParam LocalDateTime time) {
        return orderS.findByCreationDateBefore(time);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/after")
    public List<Order> createdAfter(@RequestParam LocalDateTime time) {
        return orderS.findByCreationDateAfter(time);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/between")
    public List<Order> createdBetween(@RequestParam LocalDateTime startTime,@RequestParam LocalDateTime endTime) {
        return orderS.findByCreationDateIsBetween(startTime,endTime);
    }

    @PatchMapping("/{id}/{active}")
    public ResponseEntity<Order> activeStatus(@PathVariable long id,@PathVariable boolean active) {
        if (!orderS.findBy(id).isPresent())
            return ResponseEntity.badRequest().build();
        Order updated= orderS.findBy(id).get();
        updated.setActiveStatus(active);
        return ResponseEntity.ok(updated);
    }


    //Not sure obout products
    @PostMapping("/save")
    public ResponseEntity<Order> saveAndFlush(@RequestBody Order order) {
        order.setCreationDate(LocalDateTime.now());
        if (order.getCustomer().getId() == null || customerS.findBy(order.getCustomer().getId()).isPresent())
            return ResponseEntity.badRequest().build();
        order.setCustomer(customerS.findBy(order.getCustomer().getId()).get());
        if (order.getAddress().getId() ==null || addressS.findBy(order.getAddress().getId()).isPresent())
            return ResponseEntity.badRequest().build();
        order.setAddress(addressS.findBy(order.getAddress().getId()).get());
        return ResponseEntity.ok(orderS.saveAndFlush(order));
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/saveAll")
    public List<Order> saveAll(@RequestBody List<Order> list) {
        List<Order> addedList = new ArrayList<>();
        for (Order order:list){
            if (saveAndFlush(order).getBody() != null)
                addedList.add(order);
        }
        return addedList;
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<Order> updateById(@PathVariable long id, @RequestBody Order patchOrder) {
        if (!orderS.findBy(id).isPresent())
            return ResponseEntity.badRequest().build();

        Order oldOrder = orderS.findBy(id).get();

        oldOrder.setActiveStatus(patchOrder.isActiveStatus());

        if (patchOrder.getCreationDate() != null)
            oldOrder.setCreationDate(patchOrder.getCreationDate());
        if (patchOrder.getAddress() != null)
            oldOrder.setAddress(patchOrder.getAddress());
        if (patchOrder.getProductList() != null)
            oldOrder.setProductList(patchOrder.getProductList());

        return ResponseEntity.ok(orderS.saveAndFlush(oldOrder));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Order> deleteById(@PathVariable Long id) {
        if (orderS.findBy(id).isPresent()) {
            orderS.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.badRequest().build();
    }


}
