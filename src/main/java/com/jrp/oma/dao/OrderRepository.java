package com.jrp.oma.dao;

import com.jrp.oma.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    List<Order> findByStatus(Order.Status status);

    List<Order> findByCustomerId(long id);

    List<Order> findByAddressId(long id);

    List<Order> findByCreationDateBefore (LocalDateTime dateTime);

    List<Order> findByCreationDateAfter (LocalDateTime dateTime);

    List<Order> findByCreationDateIsBetween (LocalDateTime startTime,LocalDateTime endTime);
}
