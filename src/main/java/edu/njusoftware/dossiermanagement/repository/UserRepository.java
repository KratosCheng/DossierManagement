package edu.njusoftware.dossiermanagement.repository;

import edu.njusoftware.dossiermanagement.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    User findFirstByJobNum(String jobNum);

    List<User> findAllByRoleName(String roleName);
}
