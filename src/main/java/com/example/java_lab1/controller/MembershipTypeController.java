package com.example.java_lab1.controller;

import com.example.java_lab1.dto.MembershipTypeResponse;
import com.example.java_lab1.service.MembershipTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/membership-types")
@RequiredArgsConstructor
public class MembershipTypeController {

    private final MembershipTypeService service;

    @GetMapping
    public List<MembershipTypeResponse> getAll() {
        return service.getAllActive();
    }

    @GetMapping("/{id}")
    public MembershipTypeResponse getOne(@PathVariable Long id) {
        return service.getById(id);
    }
}