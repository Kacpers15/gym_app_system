package com.example.java_lab1.controller;

import com.example.java_lab1.dto.ClassBookingResponse;
import com.example.java_lab1.dto.GroupClassResponse;
import com.example.java_lab1.security.AuthUtils;
import com.example.java_lab1.service.ClassBookingService;
import com.example.java_lab1.service.GroupClassService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/group-classes")
@RequiredArgsConstructor
public class GroupClassController {

    private final GroupClassService groupClassService;
    private final ClassBookingService bookingService;

    @GetMapping
    public List<GroupClassResponse> list(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long trainerId) {
        return groupClassService.listUpcoming(name, trainerId);
    }

    @GetMapping("/{id}")
    public GroupClassResponse get(@PathVariable Long id) {
        return groupClassService.getById(id);
    }

    @PostMapping("/{id}/bookings")
    @ResponseStatus(HttpStatus.CREATED)
    public ClassBookingResponse enroll(@PathVariable Long id, Authentication auth) {
        return bookingService.enroll(AuthUtils.currentUserId(auth), id);
    }

    @DeleteMapping("/{id}/bookings/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancel(@PathVariable Long id, Authentication auth) {
        bookingService.cancelMyEnrollment(AuthUtils.currentUserId(auth), id);
    }

    @GetMapping("/bookings/me")
    public List<ClassBookingResponse> myBookings(Authentication auth) {
        return bookingService.myBookings(AuthUtils.currentUserId(auth));
    }
}