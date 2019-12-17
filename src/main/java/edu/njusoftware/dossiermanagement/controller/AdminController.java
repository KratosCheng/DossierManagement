package edu.njusoftware.dossiermanagement.controller;

import edu.njusoftware.dossiermanagement.domain.Case;
import edu.njusoftware.dossiermanagement.service.ICaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ICaseService caseService;

    @RequestMapping("/addCase")
    public boolean addCase(Case caseInfo) {
        return caseService.saveCase(caseInfo);
    }
}
