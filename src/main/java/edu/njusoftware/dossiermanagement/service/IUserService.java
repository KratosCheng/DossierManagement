package edu.njusoftware.dossiermanagement.service;

import edu.njusoftware.dossiermanagement.domain.Account;
import edu.njusoftware.dossiermanagement.domain.DossierOperationRecord;
import edu.njusoftware.dossiermanagement.domain.req.AccountQueryCondition;
import edu.njusoftware.dossiermanagement.domain.req.RecordQueryCondition;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IUserService {
    Account addUser(Account user);

    Account modifyUserRole(String jobNum, String roleName);

    Account getUserByJobNum(String jobNum);

    List<Account> getAllUsers();

    List<Account> getAllUsersByRoleName(String roleName);

    Page<DossierOperationRecord> getOperationRecords(RecordQueryCondition recordQueryCondition);

    Page<Account> getUsers(AccountQueryCondition accountQueryCondition);

    boolean saveUser(Account user);

    boolean removeUserByJobNum(String jobNum);
}
