package com.example.java_lab1.controller;

import com.example.java_lab1.dto.MembershipTypeRequest;
import com.example.java_lab1.model.MembershipType;
import com.example.java_lab1.service.MembershipTypeService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/membership-types")
public class MembershipTypeController {

    private final MembershipTypeService service;

    public MembershipTypeController(MembershipTypeService service) {
        this.service = service;
    }

    @GetMapping
    public List<MembershipType> getAll() {
        return service.getAllActive();
    }

    @GetMapping("/{id}")
    public MembershipType getOne(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MembershipType create(@RequestBody MembershipTypeRequest req) {
        return service.create(req);
    }

    @PutMapping("/{id}")
    public MembershipType update(@PathVariable Long id, @RequestBody MembershipTypeRequest req) {
        return service.update(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}