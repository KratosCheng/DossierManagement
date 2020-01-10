package edu.njusoftware.dossiermanagement.controller;

import edu.njusoftware.dossiermanagement.domain.rsp.BaseResponse;
import edu.njusoftware.dossiermanagement.util.Constants;
import edu.njusoftware.dossiermanagement.domain.CaseInfo;
import edu.njusoftware.dossiermanagement.domain.User;
import edu.njusoftware.dossiermanagement.service.ICaseService;
import edu.njusoftware.dossiermanagement.service.IUserService;
import edu.njusoftware.dossiermanagement.util.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.DigestUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    public boolean addCase(CaseInfo caseInfo) {
        return caseService.saveCase(caseInfo);
    }

    /**
     * 新增账户
     * @param user
     * @param rs
     * @return
     */
    @RequestMapping(value = "/addUser")
    public BaseResponse addUser(@ModelAttribute("caseInfo") @Validated User user, BindingResult rs) {
        if (rs.hasErrors()) {
            StringBuilder errorMsg =
                    new StringBuilder(SecurityUtils.getLoginUserName() + " attempt to create user with error: ");
            for (ObjectError error : rs.getAllErrors()) {
                errorMsg.append(error.getDefaultMessage()).append("|");
            }
            logger.error(errorMsg.toString());
            return new BaseResponse(Constants.CODE_FAIL, "添加失败，请检查账户属性！");
        }
        logger.info(SecurityUtils.getLoginUserName() + "created user: " + user.getJobNum());
        return userService.saveUser(user) ? new BaseResponse(Constants.CODE_SUCCESS, "添加成功！")
                : new BaseResponse(Constants.CODE_FAIL, "添加失败！");
    }

    @RequestMapping("/modifyUserRole")
    public boolean modifyUserRole(@RequestParam String jobNum, @RequestParam String roleName) {
        if (!roleList.contains(roleName)) {
            return false;
        }
        return userService.modifyUserRole(jobNum, roleName) != null;
    }
}
