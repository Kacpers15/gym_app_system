package com.example.java_lab1.controller.admin;

import com.example.java_lab1.dto.ClassBookingResponse;
import com.example.java_lab1.dto.GroupClassRequest;
import com.example.java_lab1.dto.GroupClassResponse;
import com.example.java_lab1.service.ClassBookingService;
import com.example.java_lab1.service.GroupClassService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/group-classes")
@RequiredArgsConstructor
public class AdminGroupClassController {

    private final GroupClassService groupClassService;
    private final ClassBookingService bookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GroupClassResponse create(@Valid @RequestBody GroupClassRequest req) {
        return groupClassService.create(req);
    }

    @PutMapping("/{id}")
    public GroupClassResponse update(@PathVariable Long id, @Valid @RequestBody GroupClassRequest req) {
        return groupClassService.update(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        groupClassService.delete(id);
    }

    @PatchMapping("/{id}/cancel")
    public GroupClassResponse cancel(@PathVariable Long id) {
        return groupClassService.cancel(id);
    }

    @GetMapping("/{id}/bookings")
    public List<ClassBookingResponse> bookings(@PathVariable Long id) {
        return bookingService.listForClass(id);
    }
}