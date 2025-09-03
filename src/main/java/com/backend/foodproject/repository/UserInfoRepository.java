package com.backend.foodproject.repository;

import com.backend.foodproject.entity.UserInfo;
import com.backend.foodproject.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserInfoRepository extends JpaRepository<UserInfo, Integer> {
    Optional<UserInfo> findByEmail(String email);
    Optional<UserInfo> findByVerificationToken(String token);
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByRole(Role role);
}
