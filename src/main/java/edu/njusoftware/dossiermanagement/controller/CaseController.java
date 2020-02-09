package edu.njusoftware.dossiermanagement.controller;

import edu.njusoftware.dossiermanagement.domain.CaseInfo;
import edu.njusoftware.dossiermanagement.domain.req.CaseQueryCondition;
import edu.njusoftware.dossiermanagement.domain.rsp.BaseResponse;
import edu.njusoftware.dossiermanagement.service.ICaseService;
import edu.njusoftware.dossiermanagement.util.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/case")
public class CaseController {
    private static final Logger logger = LoggerFactory.getLogger(CaseController.class);

    @Autowired
    private ICaseService caseService;

    @RequestMapping("/info/{caseNum}")
    @ResponseBody
    public CaseInfo getCaseInfo(@PathVariable String caseNum) {
        if (StringUtils.isEmpty(caseNum)) {
            return null;
        }
        return caseService.getCaseInfo(caseNum);
    }

    /**
     * 获取所有的案件
     *
     * @return
     */
    @RequestMapping("/all")
    public List<CaseInfo> getAllCases() {
        return caseService.getAllCases();
    }

    /**
     * 获取某类型所有的案件
     *
     * @return
     */
    @RequestMapping("/list/{type}")
    public List<CaseInfo> getAllCases(@PathVariable String type) {
        return caseService.getCasesByType(type);
    }

    @RequestMapping(value = "/list")
    public String getCaseList(Model model, @ModelAttribute CaseQueryCondition caseQueryCondition) {
        Page<CaseInfo> caseList = caseService.getCaseList(caseQueryCondition);
        model.addAttribute("caseList", caseList);
        model.addAttribute("caseInfo", new CaseInfo());
        model.addAttribute("title", "案件列表");
        return "index";
    }

    /**
     * 新增案件
     * @param caseInfo
     * @param rs
     * @return
     */
    @RequestMapping(value = "/add")
    public String addCase(@ModelAttribute("caseInfo") @Validated CaseInfo caseInfo, BindingResult rs) {
        if (rs.hasErrors()) {
            StringBuilder errorMsg =
                    new StringBuilder(SecurityUtils.getLoginUserName() + " attempt to create case with error: ");
            for (ObjectError error : rs.getAllErrors()) {
                errorMsg.append(error.getDefaultMessage()).append("|");
            }
            logger.error(errorMsg.toString());
            return "addCase";
        }
        logger.info(SecurityUtils.getLoginUserName() + "created case: #" + caseInfo.getCaseNum());
        return caseService.saveCase(caseInfo) ? "index" : "redirect:/error";
    }

    /**
     * 删除案件
     * @param caseNum
     * @return
     */
    @RequestMapping(value = "/remove/{caseNum}")
    @ResponseBody
    public BaseResponse removeCase(@PathVariable String caseNum) {
        return caseService.removeCase(caseNum);
    }

    /**
     * form表单提交 Date类型数据绑定
     *
     * @param binder
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

}
