package com.jrp.oma.controllers;

import com.jrp.oma.entities.Order;
import com.jrp.oma.entities.Product;
import com.jrp.oma.services.AddressService;
import com.jrp.oma.services.CustomerService;
import com.jrp.oma.services.OrderService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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


    public OrderController(OrderService orderS, CustomerService customerS, AddressService addressS) {
        this.orderS = orderS;
        this.customerS = customerS;
        this.addressS = addressS;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public Iterable<Order> findAll() {
        return orderS.findAll();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/pageable")
    public Iterable<Order> findAllPaginated(@RequestParam(value = "page", defaultValue = "0") int page,
                                   @RequestParam(value = "size", defaultValue = "50") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return orderS.findAll(pageable);
    }

    @ResponseStatus(HttpStatus.FOUND)
    @GetMapping("/{id}")
    public Optional<Order> findById(@PathVariable("id") Long id) {
        return orderS.findBy(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/status/{status}")
    public List<Order> findByStatus(@PathVariable String status) {
        status = status.toUpperCase();
        return orderS.findByStatus(status);
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
    @GetMapping("/created-before")
    public List<Order> createdBefore(@RequestParam LocalDateTime date) {
        return orderS.findByCreationDateBefore(date);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/created-after")
    public List<Order> createdAfter(@RequestParam LocalDateTime date) {
        return orderS.findByCreationDateAfter(date);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/created-between")
    public List<Order> createdBetween(@RequestParam LocalDateTime startDate, @RequestParam LocalDateTime endDate) {
        return orderS.findByCreationDateIsBetween(startDate, endDate);
    }

    @PatchMapping("/{id}/update-status/{status}")
    public ResponseEntity<Order> activeStatus(@PathVariable long id, @PathVariable String status) {
        if (!orderS.findBy(id).isPresent())
            return ResponseEntity.badRequest().build();
        Order updated = orderS.findBy(id).get();
        updated.setStatus(Order.Status.valueOf(status.toUpperCase()));
        return ResponseEntity.ok(updated);
    }

    /**
     * Addition can be multiple of same item. But deleting is for only
     *
     * @param id
     * @param operation
     * @param productList
     * @return
     */
    @PatchMapping(value = "/{id}/add-remove-products/{operation}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<Order> updateProductList(@PathVariable Long id,
                                                                 @PathVariable String operation,
                                                                 @RequestBody List<Product> productList) {
        if (!orderS.findBy(id).isPresent())
            return ResponseEntity.badRequest().build();

        Order order = orderS.findBy(id).get();

        if (operation.equalsIgnoreCase("add")) {
            order.getProductList().addAll(productList);
        } else if (operation.equalsIgnoreCase("remove")) {
            productList.removeIf(p1 -> order.getProductList().stream().noneMatch(p2 ->
                    p2.getId() == p1.getId()));
            order.getProductList().removeIf(p1 -> productList.removeIf(p2 ->
                    p2.getId() == p1.getId()));
        } else return ResponseEntity.badRequest().build();

        order.setProductList(orderS.checkProductListUpdatePriceAndTax(order));

        return ResponseEntity.ok(orderS.saveAndFlush(order));
    }

    @PostMapping("/save")
    public ResponseEntity<Order> saveAndFlush(@RequestBody Order order) {
        order.setCreationDate(LocalDateTime.now());
        order.setStatus(Order.Status.ORDER_PENDING);

        if (order.getCustomer().getId() == null || !customerS.findBy(order.getCustomer().getId()).isPresent())
            return ResponseEntity.badRequest().build();
        order.setCustomer(customerS.findBy(order.getCustomer().getId()).get());

        if (order.getAddress().getId() == null || !addressS.findBy(order.getAddress().getId()).isPresent())
            return ResponseEntity.badRequest().build();
        order.setAddress(addressS.findBy(order.getAddress().getId()).get());

        order.setProductList(orderS.checkProductListUpdatePriceAndTax(order));

        return ResponseEntity.ok(orderS.saveAndFlush(order));
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/save-all")
    public List<Order> saveAll(@RequestBody List<Order> list) {
        List<Order> addedList = new ArrayList<>();
        for (Order order : list) {
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

        oldOrder.setStatus(patchOrder.getStatus());

        if (patchOrder.getCreationDate() != null)
            oldOrder.setCreationDate(patchOrder.getCreationDate());
        if (patchOrder.getAddress() != null && addressS.findBy(patchOrder.getAddress().getId()).isPresent())
            oldOrder.setAddress(addressS.findBy(patchOrder.getAddress().getId()).get());

        if (patchOrder.getProductList() != null) {
            oldOrder.setProductList(orderS.checkProductListUpdatePriceAndTax(patchOrder));
        }

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
