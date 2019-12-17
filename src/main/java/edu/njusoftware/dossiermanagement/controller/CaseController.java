package edu.njusoftware.dossiermanagement.controller;

import edu.njusoftware.dossiermanagement.domain.Case;
import edu.njusoftware.dossiermanagement.service.ICaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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

    @RequestMapping("/all")
    public List<Case> getAllCases() {
        return caseService.getAllCases();
    }

    @RequestMapping("/list/{type}")
    public List<Case> getAllCases(@PathVariable String type) {
        return caseService.getCasesByType(type);
    }
}
