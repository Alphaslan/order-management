package com.jrp.oma.services;

import com.jrp.oma.dao.OrderRepository;
import com.jrp.oma.entities.Order;
import com.jrp.oma.entities.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    OrderRepository orderR;

    @Autowired
    ProductService productS;


    public Optional<Order> findBy(long id) {
        return orderR.findById(id);
    }


    //update this code
    public List<Order> findByStatus(String status) {
        return orderR.findByStatus(Order.Status.valueOf(status));
    }

    public List<Order> findByCustomer(long customerId) {
        return orderR.findByCustomerId(customerId);
    }

    public List<Order> findByAddress(long addressId) {
        return orderR.findByAddressId(addressId);
    }

    public List<Order> findAll() {
        return orderR.findAll();
    }

    public Iterable<Order> findAll(Pageable pageable) {
        return orderR.findAll(pageable);
    }

    public Iterable<Order> findAll(Sort sort) {
        return orderR.findAll(sort);
    }

    public List<Order> findByCreationDateBefore(LocalDateTime dateTime) {
        return orderR.findByCreationDateBefore(dateTime);
    }

    public List<Order> findByCreationDateAfter(LocalDateTime dateTime) {
        return orderR.findByCreationDateAfter(dateTime);
    }

    public List<Order> findByCreationDateIsBetween(LocalDateTime startTime, LocalDateTime endTime) {
        return orderR.findByCreationDateIsBetween(startTime, endTime);
    }

    public BigDecimal priceCalculator(Order order) {
        List<Product> productList = order.getProductList();
        BigDecimal price = BigDecimal.valueOf(0);
        for (Product product : productList) {
            price = price.add(product.getPrice());
        }
        return price;
    }

    public List<Product> checkProductListUpdatePriceAndTax(Order order){
        order.getProductList().removeIf(product -> !productS.findBy(product.getId()).isPresent());

        List<Product> productList = new ArrayList<>();
        for (Product product:order.getProductList()) {
            productList.add(productS.findBy(product.getId()).get());
        }
        order.setProductList(productList);

        order.setPrice(priceCalculator(order));
        order.setTax(taxPercentage(order).multiply(order.getPrice()));
        return order.getProductList();
    }

    public Order saveAndFlush(Order order) {
        return orderR.saveAndFlush(order);
    }

    public void deleteById(Long id) {
        orderR.deleteById(id);
    }

    /**
     * Could have been get this data from website/file
     *
     * @param order
     * @return
     */
    public BigDecimal taxPercentage(Order order) {
        switch (order.getAddress().getState()) {
            case AK:
            case MT:
            case NH:
            case OR:
            case DE:
                return BigDecimal.valueOf(0);
            case CO:
                return BigDecimal.valueOf(0.029);
            case AL:
            case GA:
            case HI:
            case NY:
            case WY:
                return BigDecimal.valueOf(0.04);
            case MO:
                return BigDecimal.valueOf(0.04225);
            case LA:
                return BigDecimal.valueOf(0.0445);
            case OK:
            case SD:
                return BigDecimal.valueOf(0.045);
            case NC:
                return BigDecimal.valueOf(0.0475);
            case ND:
            case WI:
                return BigDecimal.valueOf(0.05);
            case NM:
                return BigDecimal.valueOf(0.05125);
            case VA:
                return BigDecimal.valueOf(0.053);
            case ME:
            case NE:
                return BigDecimal.valueOf(0.055);
            case AZ:
                return BigDecimal.valueOf(0.056);
            case OH:
                return BigDecimal.valueOf(0.0575);
            case DC:
            case FL:
            case ID:
            case KY:
            case MD:
            case MI:
            case PA:
            case SC:
            case VT:
            case WV:
                return BigDecimal.valueOf(0.06);
            case UT:
                return BigDecimal.valueOf(0.061);
            case IL:
            case MA:
            case TX:
                return BigDecimal.valueOf(0.0625);
            case CT:
                return BigDecimal.valueOf(0.0635);
            case AR:
            case KS:
            case WA:
                return BigDecimal.valueOf(0.065);
            case NJ:
                return BigDecimal.valueOf(0.06625);
            case NV:
                return BigDecimal.valueOf(0.0685);
            case MN:
                return BigDecimal.valueOf(0.06875);
            case IN:
            case MS:
            case RI:
            case TN:
                return BigDecimal.valueOf(0.07);
            case CA:
                return BigDecimal.valueOf(0.0725);
        }
        return BigDecimal.valueOf(-1);
    }

}
