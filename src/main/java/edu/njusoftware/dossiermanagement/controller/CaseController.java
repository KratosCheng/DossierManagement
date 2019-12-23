package edu.njusoftware.dossiermanagement.controller;

import edu.njusoftware.dossiermanagement.domain.Case;
import edu.njusoftware.dossiermanagement.service.ICaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/case")
public class CaseController {
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
     * @return
     */
    @RequestMapping("/all")
    public List<Case> getAllCases() {
        return caseService.getAllCases();
    }

    /**
     * 获取某类型所有的案件
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
}
