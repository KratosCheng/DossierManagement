package edu.njusoftware.dossiermanagement.controller;

import edu.njusoftware.dossiermanagement.domain.Dossier;
import edu.njusoftware.dossiermanagement.domain.DossierOperationRecord;
import edu.njusoftware.dossiermanagement.service.IDossierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dossier")
public class DossierController {

    @Autowired
    private IDossierService iDossierService;

    @RequestMapping("/common/info/{id}")
    public Dossier getDossier(@PathVariable long id) {
        return iDossierService.getDossier(id);
    }

    @RequestMapping("/common/all/{caseNum}")
    public List<Dossier> getDossersByCaseNum(@PathVariable String caseNum) {
        return iDossierService.getDossiersByCaseNum(caseNum);
    }

    /**
     * 查找一个案件中所有卷宗的操作记录
     * @param caseNum
     * @return
     */
    @RequestMapping("/his/all/{caseNum}")
    public List<DossierOperationRecord> getRecordsByCaseNum(@PathVariable String caseNum) {
        return iDossierService.getDossierOperationRecordsByCaseNum(caseNum);
    }

    /**
     * 查找一个卷宗所有的操作记录
     * @param dossierId
     * @return
     */
    @RequestMapping("/his/dossier/{dossierId}")
    public List<DossierOperationRecord> getRecordsByDossierId(@PathVariable int dossierId) {
        return iDossierService.getDossierOperationRecordsByDossierId(dossierId);
    }

    /**
     * 查找一个用户所有的卷宗操作记录
     * @param jobNum
     * @return
     */
    @RequestMapping("/his/dossier/{jobNum}")
    public List<DossierOperationRecord> getRecordsByDossierId(@PathVariable String jobNum) {
        return iDossierService.getDossierOperationRecordsByJobNum(jobNum);
    }

    @RequestMapping("/add")
    public boolean add(Dossier dossier) {
        return iDossierService.saveDossier(dossier);
    }

    @RequestMapping("/remove/1")
    public String remove() {
        return "删除卷宗（admin，judge）";
    }
}
