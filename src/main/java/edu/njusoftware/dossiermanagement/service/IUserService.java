package edu.njusoftware.dossiermanagement.service;

import edu.njusoftware.dossiermanagement.domain.DossierOperationRecord;
import edu.njusoftware.dossiermanagement.domain.User;

import java.util.List;

public interface IUserService {
    User addUser(User user);

    User modifyUserRole(String jobNum, String roleName);
}
