package com.example.java_lab1.dto;

import com.example.java_lab1.model.MembershipStatus;
import java.math.BigDecimal;
import java.time.LocalDate;

public record MembershipResponse(
        Long id,
        Long membershipTypeId,
        String planName,
        LocalDate startDate,
        LocalDate endDate,
        BigDecimal pricePaid,
        MembershipStatus status
) {}