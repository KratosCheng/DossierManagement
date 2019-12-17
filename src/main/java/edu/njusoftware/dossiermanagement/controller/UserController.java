package edu.njusoftware.dossiermanagement.controller;

import edu.njusoftware.dossiermanagement.config.Constants;
import edu.njusoftware.dossiermanagement.domain.User;
import edu.njusoftware.dossiermanagement.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(name = "/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private List<String> roleList =
            Arrays.asList(Constants.ROLE_ADMIN, Constants.ROLE_JUDGE, Constants.ROLE_PARTNER, Constants.ROLE_VISITOR);

    @Qualifier("userServiceImpl")
    @Autowired
    private IUserService userService;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public boolean addUser(User user) {
        String encodePassword = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(encodePassword);
        return userService.addUser(user) != null;
    }

    @RequestMapping("/modify/role")
    public boolean modifyUserRole(@RequestParam String jobNum, @RequestParam String roleName) {

        if (!roleList.contains(roleName)) {
            return false;
        }
        return userService.modifyUserRole(jobNum, roleName) == null;
    }

    @RequestMapping("/test")
    public boolean test() {

        return true;
    }
}
