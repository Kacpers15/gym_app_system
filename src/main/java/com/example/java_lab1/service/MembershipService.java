package com.example.java_lab1.service;

import com.example.java_lab1.dto.MembershipResponse;
import com.example.java_lab1.dto.PurchaseMembershipRequest;
import com.example.java_lab1.exception.ConflictException;
import com.example.java_lab1.exception.ResourceNotFoundException;
import com.example.java_lab1.model.*;
import com.example.java_lab1.repository.MembershipRepository;
import com.example.java_lab1.repository.MembershipTypeRepository;
import com.example.java_lab1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MembershipService {

    private final MembershipRepository membershipRepository;
    private final MembershipTypeRepository typeRepository;
    private final UserRepository userRepository;

    @Transactional
    public MembershipResponse purchase(Long userId, PurchaseMembershipRequest req) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        MembershipType type = typeRepository.findById(req.membershipTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Membership type not found"));

        if (!type.isActive()) {
            throw new ConflictException("Membership type is not available");
        }

        // poprzednie ACTIVE → EXPIRED
        membershipRepository.findByUserId(userId).stream()
                .filter(m -> m.getStatus() == MembershipStatus.ACTIVE)
                .forEach(m -> m.setStatus(MembershipStatus.EXPIRED));

        LocalDate start = LocalDate.now();
        Membership membership = new Membership();
        membership.setUser(user);
        membership.setType(type);
        membership.setStartDate(start);
        membership.setEndDate(start.plusDays(type.getDurationDays()));
        membership.setPricePaid(type.getPrice()); // snapshot ceny
        membership.setStatus(MembershipStatus.ACTIVE);

        return toResponse(membershipRepository.save(membership));
    }

    @Transactional(readOnly = true)
    public List<MembershipResponse> myMemberships(Long userId) {
        return membershipRepository.findByUserId(userId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public MembershipResponse myActive(Long userId) {
        LocalDate today = LocalDate.now();
        return membershipRepository.findByUserId(userId).stream()
                .filter(m -> m.getStatus() == MembershipStatus.ACTIVE)
                .filter(m -> !m.getStartDate().isAfter(today) && !m.getEndDate().isBefore(today))
                .findFirst()
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("No active membership"));
    }

    private MembershipResponse toResponse(Membership m) {
        return new MembershipResponse(
                m.getId(),
                m.getType().getId(),
                m.getType().getName(),
                m.getStartDate(),
                m.getEndDate(),
                m.getPricePaid(),
                m.getStatus()
        );
    }
}