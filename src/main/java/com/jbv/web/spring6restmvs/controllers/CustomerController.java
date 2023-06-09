/*
 * Copyright (c) 2023. jbv
 */

package com.jbv.web.spring6restmvs.controllers;

import com.jbv.web.spring6restmvs.exceptions.NotFoundException;
import com.jbv.web.spring6restmvs.dtos.CustomerDTO;
import com.jbv.web.spring6restmvs.services.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
public class CustomerController {

    public static final String CUSTOMER_PATH = "/api/v1/customer";
    public static final String CUSTOMER_PATH_ID = CUSTOMER_PATH + "/{customerId}";

    private final CustomerService customerService;

    @GetMapping(CUSTOMER_PATH)
    public List<CustomerDTO> getCustomers() {

        log.debug("Get Customers in controller was called.");

        return customerService.getCustomers();
    }

    @GetMapping(CUSTOMER_PATH_ID)
    public CustomerDTO getCustomerById(@PathVariable UUID customerId) {

        log.debug("Get customer by id in controller was called.");

        return customerService.getCustomerById(customerId).orElseThrow(NotFoundException::new);
    }

    @PostMapping(CUSTOMER_PATH)
    public ResponseEntity createCustomer(@Validated @RequestBody CustomerDTO customerDTO) {

        log.debug("Create customer in controller was called.");

        CustomerDTO newCustomerDTO = customerService.createCustomer(customerDTO);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Location", CUSTOMER_PATH + "/" + newCustomerDTO.getCustomerId().toString());

        return new ResponseEntity<>(httpHeaders, HttpStatus.CREATED);
    }


    @PutMapping(CUSTOMER_PATH_ID)
    public ResponseEntity updateCustomerById(@PathVariable("customerId") UUID customerId,
                                             @RequestBody CustomerDTO customerDTO) {

        log.debug("Update customer by id in controller was called.");

        if (customerService.updateCustomerById(customerId, customerDTO).isEmpty()) {
            throw new NotFoundException();
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @DeleteMapping(CUSTOMER_PATH_ID)
    public ResponseEntity deleteCustomerById(@PathVariable UUID customerId) {

        log.debug("Delete customer by id in controller was called.");

        if (!customerService.deleteCustomerById(customerId)) {
            throw new NotFoundException();
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping(CUSTOMER_PATH_ID)
    public ResponseEntity patchCustomerById(@PathVariable UUID customerId,
                                            @RequestBody CustomerDTO customerDTO) {
        log.debug("Patch customer by id in controller was called.");

        customerService.patchCustomerById(customerId, customerDTO);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}