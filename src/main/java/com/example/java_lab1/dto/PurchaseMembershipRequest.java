package com.example.java_lab1.dto;

import jakarta.validation.constraints.NotNull;

public record PurchaseMembershipRequest(
        @NotNull Long membershipTypeId
) {}