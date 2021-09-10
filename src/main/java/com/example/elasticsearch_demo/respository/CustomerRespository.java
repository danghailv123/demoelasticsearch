package com.example.elasticsearch_demo.respository;

import com.example.elasticsearch_demo.document.CustomerDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CustomerRespository extends ElasticsearchRepository<CustomerDocument,String> {

    List<CustomerDocument> findByName(String name);
}
