package edu.njusoftware.dossiermanagement.controller;

import edu.njusoftware.dossiermanagement.util.Constants;
import edu.njusoftware.dossiermanagement.domain.Case;
import edu.njusoftware.dossiermanagement.domain.User;
import edu.njusoftware.dossiermanagement.service.ICaseService;
import edu.njusoftware.dossiermanagement.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private List<String> roleList =
            Arrays.asList(Constants.ROLE_ADMIN, Constants.ROLE_JUDGE, Constants.ROLE_PARTNER, Constants.ROLE_VISITOR);

    @Autowired
    private ICaseService caseService;

    @Qualifier("userServiceImpl")
    @Autowired
    private IUserService userService;

    @RequestMapping("/addCase")
    public boolean addCase(Case caseInfo) {
        return caseService.saveCase(caseInfo);
    }

    @RequestMapping(value = "/addUser", method = RequestMethod.POST)
    public boolean addUser(User user) {
        String encodePassword = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(encodePassword);
        return userService.addUser(user) != null;
    }

    @RequestMapping("/modifyUserRole")
    public boolean modifyUserRole(@RequestParam String jobNum, @RequestParam String roleName) {
        if (!roleList.contains(roleName)) {
            return false;
        }
        return userService.modifyUserRole(jobNum, roleName) != null;
    }
}
