package edu.njusoftware.dossiermanagement.repository;

import edu.njusoftware.dossiermanagement.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    List<Role> findAllByName(String name);
}
