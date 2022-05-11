package com.jrp.oma.controllers;

import com.jrp.oma.entities.Address;
import com.jrp.oma.services.AddressService;
import com.jrp.oma.services.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/address")
public class AddressController {

    private final AddressService addressS;
    private final CustomerService customerS;

    public AddressController(AddressService addressS, CustomerService customerS) {
        this.addressS = addressS;
        this.customerS = customerS;
    }

    // findBy *id *street *city *state zip *customer
    //add one all
    //update patch
    //delete one
    //unique
    //sort
    //pageable

    @ResponseStatus(HttpStatus.OK)
    @GetMapping()
    public List<Address> findAll() {
        return addressS.findAll();
    }

    @ResponseStatus(HttpStatus.FOUND)
    @GetMapping("/{id}")
    public Optional<Address> findById(@PathVariable("id") long id) {
        return addressS.findBy(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/customerId/{id}")
    public List<Address> findByCustomerId(@PathVariable("id") long id) {
        return addressS.findByCustomerId(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/street/{street}")
    public List<Address> findAllByStreet(@PathVariable("street") String street) {
        return addressS.findAllByStreet(street);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/city/{city}")
    public List<Address> findAllByCity(@PathVariable("city") String city) {
        return addressS.findAllByCity(city);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/state/{state}")
    public List<Address> findAllByState(@PathVariable("state") String state) {
        return addressS.findAllByState(state);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/cityAndStreet/{city}/{street}")
    public List<Address> findAllByCityAndStreet(@PathVariable String city, @PathVariable("street") String street) {
        return addressS.findAllByCityAndStreet(city, street);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/stateAndCity/{state}/{city}")
    public List<Address> findAllByStateAndCity(@PathVariable String state, @PathVariable("city") String city) {
        return addressS.findAllByStateAndCity(state, city);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/zipCode/{zipCode}")
    public List<Address> findAllByZipCode(@PathVariable Integer zipCode) {
        return addressS.findAllByZipCode(zipCode);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/zipCodeStartsWith/{zipCode}")
    public List<Address> findAllByZipCodeBeginsWith(@PathVariable Integer zipCode) {
        return addressS.findAllByZipCodeStartingWith(zipCode);
    }


    @PostMapping("/save")
    public ResponseEntity<Address> saveAndFlush(@RequestBody Address address) {
        address.setState(address.getState().toUpperCase());
        String temp = address.getCity().toLowerCase();
        StringUtils.capitalize(temp);
        address.setCity(temp);
        temp = address.getStreet().toLowerCase();
        StringUtils.capitalize(temp);
        address.setStreet(temp);
        if (address.getZipCode().toString().length() != 5)
            return ResponseEntity.badRequest().build();

        if (address.getCustomer().getId() == null || customerS.findBy(address.getCustomer().getId()).isPresent())
            return ResponseEntity.badRequest().build();
        address.setCustomer(customerS.findBy(address.getCustomer().getId()).get());

        return ResponseEntity.ok(addressS.saveAndFlush(address));
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/saveAll")
    public List<Address> saveAll(@RequestBody List<Address> list) {
        List<Address> addedList = new ArrayList<>();
        for (Address address : list) {
            if (saveAndFlush(address).getBody() != null)
                addedList.add(address);
        }
        return addedList;
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<Address> updateById(@PathVariable("id") long id, @RequestBody Address patchAddress) {
        if (!addressS.findBy(id).isPresent())
            return ResponseEntity.badRequest().build();

        Address oldAddress = addressS.findBy(id).get();

        if (patchAddress.getState() != null)
            oldAddress.setState(patchAddress.getState());
        if (patchAddress.getCity() != null)
            oldAddress.setCity(patchAddress.getCity());
        if (patchAddress.getStreet() != null)
            oldAddress.setStreet(patchAddress.getStreet());
        if (patchAddress.getZipCode() != null)
            oldAddress.setZipCode(patchAddress.getZipCode());

        return saveAndFlush(oldAddress);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Address> deleteById(@PathVariable("id") Long id) {
        if (addressS.findBy(id).isPresent()) {
            addressS.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }


}
