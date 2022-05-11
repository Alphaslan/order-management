package com.jrp.oma.dao;

import com.jrp.oma.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address,Long> {
    List<Address> findAllByState(String state);

    List<Address> findAllByStateAndCity(String state, String city);

    List<Address> findAllByCity(String city);

    List<Address> findAllByStreet(String street);

    List<Address> findAllByCityAndStreet(String city, String street);

    List<Address> findByCustomerId(long id);

    List<Address> findAllByZipCode(Integer zipCode);

    List<Address> findAllByZipCodeStartingWith(Integer zipCode);


}
