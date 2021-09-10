package com.example.elasticsearch_demo.controller;


import com.example.elasticsearch_demo.document.PersonDocument;
import com.example.elasticsearch_demo.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/persons")
public class PersonController {
    private PersonService personService;
    @Autowired
    public PersonController(PersonService personService){
        this.personService=personService;
    }

    @PostMapping()
    public ResponseEntity creaResponse(@RequestBody PersonDocument document) throws Exception {
        return new ResponseEntity(personService.createPersonDocument(document), HttpStatus.CREATED);
    }
    @PutMapping()
    public ResponseEntity updatePersonDocument(@RequestBody PersonDocument document) throws Exception {

        return new ResponseEntity(personService.updatePersonDocument(document), HttpStatus.CREATED);
    }

    @GetMapping()
    public List<PersonDocument> findAll() throws Exception {
        return personService.findAll();
    }

    @GetMapping("/name-search")
    public List<PersonDocument> searchByName(@RequestParam(value = "name") String name) throws Exception {
        return personService.findPersonDocumentByName(name);
    }

    @DeleteMapping("/{id}")
    public String deletePersonDocument(@PathVariable String id ) throws Exception {
        return personService.deletePersonDocumentDocument(id);
    }


}
