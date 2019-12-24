package edu.njusoftware.dossiermanagement.controller;

import edu.njusoftware.dossiermanagement.domain.Case;
import edu.njusoftware.dossiermanagement.service.ICaseService;
import edu.njusoftware.dossiermanagement.util.SystemSecurityUtils;
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
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private ICaseService caseService;

    @RequestMapping("/info/{caseNum}")
    @ResponseBody
    public Case getCaseInfo(@PathVariable String caseNum) {
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
    public List<Case> getAllCases() {
        return caseService.getAllCases();
    }

    /**
     * 获取某类型所有的案件
     *
     * @return
     */
    @RequestMapping("/list/{type}")
    public List<Case> getAllCases(@PathVariable String type) {
        return caseService.getCasesByType(type);
    }

    @RequestMapping(value = "/list")
    public String index(Model model, @RequestParam(value = "pageNum", defaultValue = "0") int pageNum,
                        @RequestParam(value = "pageSize", defaultValue = "2") int pageSize) {
        Page<Case> caseList = caseService.getCaseList(pageNum, pageSize);
        model.addAttribute("caseList", caseList);
        return "index::#div-case-list";
    }

    /**
     * 新增案件
     * @param caseInfo
     * @param rs
     * @return
     */
    @RequestMapping(value = "/add")
    public String index(@ModelAttribute("caseInfo") @Validated Case caseInfo, BindingResult rs) {
        if (rs.hasErrors()) {
            StringBuilder errorMsg =
                    new StringBuilder(SystemSecurityUtils.getLoginUserName() + " attempt to create case with error: ");
            for (ObjectError error : rs.getAllErrors()) {
                errorMsg.append(error.getDefaultMessage()).append("|");
            }
            logger.error(errorMsg.toString());
            return "addCase";
        }
        logger.info(SystemSecurityUtils.getLoginUserName() + "created case: #" + caseInfo.getCaseNum());
        return caseService.saveCase(caseInfo) ? "redirect:/index" : "redirect:/error";
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
