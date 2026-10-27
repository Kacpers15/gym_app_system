package com.example.java_lab1.controller.admin;

import com.example.java_lab1.dto.MembershipTypeRequest;
import com.example.java_lab1.dto.MembershipTypeResponse;
import com.example.java_lab1.service.MembershipTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/membership-types")
@RequiredArgsConstructor
public class AdminMembershipTypeController {

    private final MembershipTypeService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MembershipTypeResponse create(@Valid @RequestBody MembershipTypeRequest req) {
        return service.create(req);
    }

    @PutMapping("/{id}")
    public MembershipTypeResponse update(@PathVariable Long id,
                                         @Valid @RequestBody MembershipTypeRequest req) {
        return service.update(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}