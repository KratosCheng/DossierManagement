package edu.njusoftware.dossiermanagement.service.impl;

import edu.njusoftware.dossiermanagement.domain.Case;
import edu.njusoftware.dossiermanagement.repository.CaseRepository;
import edu.njusoftware.dossiermanagement.service.ICaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CaseServiceImpl implements ICaseService {
    @Autowired
    private CaseRepository caseRepository;

    @Override
    public Case getCaseInfo(String caseNum) {
        return caseRepository.findFirstByCaseNum(caseNum);
    }

    @Override
    public List<Case> getAllCases() {
        return caseRepository.findAll();
    }

    @Override
    public Page<Case> getCaseList(int pageNum, int pageSize) {
//        Sort sort = new Sort();
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        Page<Case> caseList = caseRepository.findAll(pageable);
        return caseList;
    }

    @Override
    public List<Case> getCasesByType(String type) {
        return null;
    }

    @Override
    public boolean saveCase(Case caseInfo) {
        return caseRepository.save(caseInfo) != null;
    }
}
