package com.example.java_lab1.service;

import com.example.java_lab1.dto.MembershipTypeRequest;
import com.example.java_lab1.model.MembershipType;
import com.example.java_lab1.repository.MembershipTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MembershipTypeService {

    private final MembershipTypeRepository repository;

    public MembershipTypeService(MembershipTypeRepository repository) {
        this.repository = repository;
    }

    public List<MembershipType> getAllActive() {
        return repository.findByActiveTrue();
    }

    public MembershipType getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono karnetu o id " + id));
    }

    public MembershipType create(MembershipTypeRequest req) {
        MembershipType type = new MembershipType();
        apply(type, req);
        return repository.save(type);
    }

    public MembershipType update(Long id, MembershipTypeRequest req) {
        MembershipType type = getById(id);
        apply(type, req);
        return repository.save(type);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    private void apply(MembershipType type, MembershipTypeRequest req) {
        type.setName(req.name());
        type.setPrice(req.price());
        type.setDurationDays(req.durationDays());
        type.setActive(req.active());
    }
}