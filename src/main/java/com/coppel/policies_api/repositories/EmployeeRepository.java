package com.coppel.policies_api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.coppel.policies_api.models.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    boolean existsByUsername(String username);

    Optional<Employee> findByUsername(String username);

    Optional<Employee> deleteByUsername(String username);
}
