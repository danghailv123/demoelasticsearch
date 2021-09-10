package com.example.elasticsearch_demo.controller;

import com.example.elasticsearch_demo.document.CustomerDocument;
import com.example.elasticsearch_demo.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/customer")
public class CustomerContreller {
    private CustomerService customerService;
    @Autowired
    public CustomerContreller(CustomerService customerService){
        this.customerService=customerService;
    }

    @PostMapping()
    public ResponseEntity createCustomer(@RequestBody CustomerDocument document)throws Exception  {
        customerService.createCustomer(document);
        return new ResponseEntity("CREATE", HttpStatus.CREATED);
    }
    @PutMapping()
    public ResponseEntity updateCustomer(@RequestBody CustomerDocument document) throws Exception {
        customerService.createCustomer(document);
        return new ResponseEntity("UPDATE", HttpStatus.OK);
    }

    @GetMapping()
    public Iterable<CustomerDocument> findAll() throws Exception {
        return customerService.getAll();
    }

    @GetMapping("/name-search")
    public List<CustomerDocument> searchByName(@RequestParam(value = "name") String name) throws Exception {
        return customerService.findByName(name);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteCustomer(@PathVariable String id ) throws Exception {
       customerService.deleteCustomer(id);
        return new ResponseEntity("UPDATE", HttpStatus.OK);
    }
}
