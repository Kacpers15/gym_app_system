package com.example.java_lab1.service;

import com.example.java_lab1.dto.MembershipTypeRequest;
import com.example.java_lab1.dto.MembershipTypeResponse;
import com.example.java_lab1.exception.ResourceNotFoundException;
import com.example.java_lab1.model.MembershipType;
import com.example.java_lab1.repository.MembershipTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MembershipTypeService {

    private final MembershipTypeRepository repository;

    @Transactional(readOnly = true)
    public List<MembershipTypeResponse> getAllActive() {
        return repository.findByActiveTrue().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public MembershipTypeResponse getById(Long id) {
        return toResponse(find(id));
    }

    @Transactional
    public MembershipTypeResponse create(MembershipTypeRequest req) {
        MembershipType type = new MembershipType();
        apply(type, req);
        return toResponse(repository.save(type));
    }

    @Transactional
    public MembershipTypeResponse update(Long id, MembershipTypeRequest req) {
        MembershipType type = find(id);
        apply(type, req);
        return toResponse(type);
    }

    @Transactional
    public void delete(Long id) {
        MembershipType type = find(id);
        type.setActive(false); // soft delete — nie kasuj historii zakupów
    }

    private MembershipType find(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Membership type not found: " + id));
    }

    private void apply(MembershipType type, MembershipTypeRequest req) {
        type.setName(req.name());
        type.setDescription(req.description());
        type.setPrice(req.price());
        type.setDurationDays(req.durationDays());
        type.setActive(req.active());
    }

    private MembershipTypeResponse toResponse(MembershipType t) {
        return new MembershipTypeResponse(
                t.getId(), t.getName(), t.getDescription(),
                t.getPrice(), t.getDurationDays(), t.isActive());
    }
}