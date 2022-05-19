package com.jrp.oma.services;

import com.jrp.oma.dao.AddressRepository;
import com.jrp.oma.entities.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressService {

    @Autowired
    AddressRepository addressR;

    public List<Address> findAllByState(String state) {
        state=state.toUpperCase();
        try {
            return addressR.findAllByState(Address.States.valueOf(state));
        }catch (IllegalArgumentException e) {
            e.getSuppressed();
        }
       return null;
    }

    public List<Address> findAllByStateAndCity(String state, String city) {
        state=state.toUpperCase();
        try {
            return addressR.findAllByStateAndCity(Address.States.valueOf(state), city);
        }catch (IllegalArgumentException e) {
            e.getSuppressed();
        }
        return null;
    }

    public List<Address> findAllByCity(String city) {
        return addressR.findAllByCity(city);
    }

    public List<Address> findAllByStreet(String street) {
        return addressR.findAllByStreet(street);
    }

    public List<Address> findAllByCityAndStreet(String city, String street) {
        return addressR.findAllByCityAndStreet(city, street);
    }

    public List<Address> findByCustomerId(long id) {
        return addressR.findByCustomerId(id);
    }

    public List<Address> findAll() {
        return addressR.findAll();
    }

    public List<Address> findAll(Sort sort) {
        return addressR.findAll(sort);
    }

    public Iterable<Address> findAll(Pageable pageable) {
        return addressR.findAll(pageable);
    }

    public List<Address> findAllByZipCode(String zipCode) {
        return addressR.findAllByZipCode(zipCode);
    }

    public List<Address> findAllByZipCodeStartingWith(String zipCode) {
        return addressR.findAllByZipCodeStartingWith(zipCode);
    }

    public Optional<Address> findBy(long id) {
        return addressR.findById(id);
    }

    public Address saveAndFlush(Address address) {
        return addressR.saveAndFlush(address);
    }

    public void deleteById(long id) {
        addressR.deleteById(id);
    }

}
