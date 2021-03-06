package com.jrp.oma.controllers;

import com.jrp.oma.entities.Address;
import com.jrp.oma.services.AddressService;
import com.jrp.oma.services.CustomerService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/customer-address")
public class AddressController {

    private final AddressService addressS;
    private final CustomerService customerS;

    public AddressController(AddressService addressS, CustomerService customerS) {
        this.addressS = addressS;
        this.customerS = customerS;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public Iterable<Address> findAll() {
        return addressS.findAll();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/pageable")
    public Iterable<Address> findAllPaginated(@RequestParam(value = "page", defaultValue = "0") int page,
                                     @RequestParam(value = "size", defaultValue = "50") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return addressS.findAll(pageable);
    }

    @ResponseStatus(HttpStatus.FOUND)
    @GetMapping("/{id}")
    public Optional<Address> findById(@PathVariable("id") long id) {
        return addressS.findBy(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/customer/{id}")
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
    @GetMapping("/city-street/{city}/{street}")
    public List<Address> findAllByCityAndStreet(@PathVariable String city, @PathVariable("street") String street) {
        return addressS.findAllByCityAndStreet(city, street);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/state-city/{state}/{city}")
    public List<Address> findAllByStateAndCity(@PathVariable String state, @PathVariable("city") String city) {
        return addressS.findAllByStateAndCity(state, city);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/zipcode/{zipCode}")
    public List<Address> findAllByZipCode(@PathVariable String zipCode) {
        return addressS.findAllByZipCode(zipCode);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/zipcode-starts-with/{zipCode}")
    public List<Address> findAllByZipCodeBeginsWith(@PathVariable String zipCode) {
        return addressS.findAllByZipCodeStartingWith(zipCode);
    }


    @PostMapping("/save")
    public ResponseEntity<Address> saveAndFlush(@RequestBody Address address) {
        address.setState(address.getState());
        String temp = address.getCity().toLowerCase();
        address.setCity(StringUtils.capitalize(temp));
        temp = address.getStreet().toLowerCase();
        for (String partStreet : temp.split(" ")) {
            temp = temp.replace(partStreet, StringUtils.capitalize(partStreet));
        }
        address.setStreet(temp);
        if (address.getZipCode().length() != 5) //for only USA
            return ResponseEntity.badRequest().build();

        if (address.getCustomer().getId() == null || !customerS.findBy(address.getCustomer().getId()).isPresent())
            return ResponseEntity.badRequest().build();

        List<Address> customerAddressList = customerS.findBy(address.getCustomer().getId()).get().getAddressList();

        //validation???
        for (Address address1 : customerAddressList) {
            if (address.getStreet().equalsIgnoreCase(address1.getStreet()) &&
                    address.getZipCode().equalsIgnoreCase(address1.getZipCode()))
                return ResponseEntity.badRequest().build();
        }

        address.setCustomer(customerS.findBy(address.getCustomer().getId()).get());

        return ResponseEntity.ok(addressS.saveAndFlush(address));
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/save-all")
    public List<Address> saveAll(@RequestBody List<Address> list) {
        list.removeIf(address -> saveAndFlush(address).getBody() == null);
        return list;
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
