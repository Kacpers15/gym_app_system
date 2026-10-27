package com.example.java_lab1.controller;

import com.example.java_lab1.dto.MembershipResponse;
import com.example.java_lab1.dto.PurchaseMembershipRequest;
import com.example.java_lab1.security.AuthUtils;
import com.example.java_lab1.service.MembershipService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/memberships")
@RequiredArgsConstructor
public class MembershipController {

    private final MembershipService membershipService;

    @PostMapping("/purchase")
    @ResponseStatus(HttpStatus.CREATED)
    public MembershipResponse purchase(@Valid @RequestBody PurchaseMembershipRequest req,
                                       Authentication auth) {
        return membershipService.purchase(AuthUtils.currentUserId(auth), req);
    }

    @GetMapping("/me")
    public List<MembershipResponse> my(Authentication auth) {
        return membershipService.myMemberships(AuthUtils.currentUserId(auth));
    }

    @GetMapping("/me/active")
    public MembershipResponse active(Authentication auth) {
        return membershipService.myActive(AuthUtils.currentUserId(auth));
    }
}