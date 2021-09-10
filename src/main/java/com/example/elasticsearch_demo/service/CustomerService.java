package com.example.elasticsearch_demo.service;


import com.example.elasticsearch_demo.document.CustomerDocument;
import com.example.elasticsearch_demo.respository.CustomerRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    private CustomerRespository customerRespository;


    @Autowired
    public CustomerService (CustomerRespository customerRespository,ElasticsearchOperations operations){
        this.customerRespository=customerRespository;
    }

    public void createCustomerIndexBulk(List<CustomerDocument> customers){

        customerRespository.saveAll(customers);
    }

    public void createCustomer(CustomerDocument customer)
    {
        customerRespository.save(customer);
    }

    public void deleteCustomer(String id){
        customerRespository.deleteById(id);
    }

    public Iterable<CustomerDocument> getAll(){
        return  customerRespository.findAll();
    }
    public List<CustomerDocument> findByName(String name){
            return customerRespository.findByName(name);
    }

    public CustomerDocument findById(String id){
        return customerRespository.findById(id).get();
    }
}
