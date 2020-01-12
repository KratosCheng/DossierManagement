package edu.njusoftware.dossiermanagement.controller;

import edu.njusoftware.dossiermanagement.domain.rsp.BaseResponse;
import edu.njusoftware.dossiermanagement.util.Constants;
import edu.njusoftware.dossiermanagement.domain.CaseInfo;
import edu.njusoftware.dossiermanagement.domain.Account;
import edu.njusoftware.dossiermanagement.service.ICaseService;
import edu.njusoftware.dossiermanagement.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
     * @return
     */
    @RequestMapping(value = "/updateUser")
    @ResponseBody
    public BaseResponse updateUser(Account user) {
        return userService.saveUser(user) ? new BaseResponse(Constants.CODE_SUCCESS, "用户信息更新成功！")
                : new BaseResponse(Constants.CODE_FAIL, "用户信息更新失败！");
    }

    @RequestMapping("/removeUser/{jobNum}")
    public BaseResponse modifyUserRole(@PathVariable String jobNum) {
        return userService.removeUserByJobNum(jobNum) ? new BaseResponse(Constants.CODE_SUCCESS, "删除成功") :
                new BaseResponse(Constants.CODE_FAIL, "删除失败！");
    }
}
