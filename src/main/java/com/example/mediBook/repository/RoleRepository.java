package com.example.mediBook.repository;

import com.example.mediBook.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByRoleType(Role.RoleType roleType);
}
