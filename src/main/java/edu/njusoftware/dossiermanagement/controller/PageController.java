package edu.njusoftware.dossiermanagement.controller;

import edu.njusoftware.dossiermanagement.domain.Case;
import edu.njusoftware.dossiermanagement.service.ICaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 前端页面跳转控制器，初步计划用PageController来处理页面的跳转，其他Controller处理数据请求
 * 页面跳转不能使用 @RestController
 */
@Controller
public class PageController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private ICaseService caseService;

    @RequestMapping({"/", "/index"})
    public String index(Model model, @RequestParam(value = "pageNum", defaultValue = "0") int pageNum,
                        @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        Page<Case> caseList = caseService.getCaseList(pageNum, pageSize);
        model.addAttribute("caseList", caseList);
        return "index";
    }

    @RequestMapping("/case/add")
    public String addCase(Model model) {
        return "addCase";
    }

    @RequestMapping("/user/{jobNum}")
    public String getUserInfo(Model model, @PathVariable String jobNum) {
        return "addCase";
    }
}
