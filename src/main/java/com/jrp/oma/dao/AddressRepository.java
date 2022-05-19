package com.jrp.oma.dao;

import com.jrp.oma.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address,Long> {
    List<Address> findAllByState(Address.States state);

    List<Address> findAllByStateAndCity(Address.States state, String city);

    List<Address> findAllByCity(String city);

    List<Address> findAllByStreet(String street);

    List<Address> findAllByCityAndStreet(String city, String street);

    List<Address> findByCustomerId(long id);

    List<Address> findAllByZipCode(String zipCode);

    List<Address> findAllByZipCodeStartingWith(String zipCode);


}
