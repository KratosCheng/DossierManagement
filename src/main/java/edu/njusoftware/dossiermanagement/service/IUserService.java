package edu.njusoftware.dossiermanagement.service;

import edu.njusoftware.dossiermanagement.domain.OperationRecord;
import edu.njusoftware.dossiermanagement.domain.User;
import edu.njusoftware.dossiermanagement.domain.req.AccountQueryCondition;
import edu.njusoftware.dossiermanagement.domain.req.RecordQueryCondition;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IUserService {
    User addUser(User user);

    User modifyUserRole(String jobNum, String roleName);

    User getUserByJobNum(String jobNum);

    List<User> getAllUsers();

    List<User> getAllUsersByRoleName(String roleName);

    Page<OperationRecord> getOperationRecords(RecordQueryCondition recordQueryCondition);

    Page<User> getUsers(AccountQueryCondition accountQueryCondition);

    boolean saveUser(User user);
}
