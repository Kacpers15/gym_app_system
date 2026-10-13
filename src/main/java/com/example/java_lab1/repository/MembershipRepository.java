package com.example.java_lab1.repository;

import com.example.java_lab1.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MembershipRepository extends JpaRepository<Membership, Long> {
    List<Membership> findByUserId(Long userId);
}