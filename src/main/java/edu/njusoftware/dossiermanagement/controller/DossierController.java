package edu.njusoftware.dossiermanagement.controller;

import edu.njusoftware.dossiermanagement.domain.Dossier;
import edu.njusoftware.dossiermanagement.domain.DossierOperationRecord;
import edu.njusoftware.dossiermanagement.domain.req.AddDirectoryReq;
import edu.njusoftware.dossiermanagement.domain.rsp.BaseResponse;
import edu.njusoftware.dossiermanagement.service.IDossierService;
import edu.njusoftware.dossiermanagement.util.Constants;
import edu.njusoftware.dossiermanagement.util.SystemSecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dossier")
public class DossierController {
    private static final Logger logger = LoggerFactory.getLogger(DossierController.class);

    @Autowired
    private IDossierService dossierService;

    @RequestMapping("/common/info/{id}")
    public Dossier getDossier(@PathVariable long id) {
        return dossierService.getDossier(id);
    }

    @RequestMapping("/common/all/{caseNum}")
    public List<Dossier> getDossersByCaseNum(@PathVariable String caseNum) {
        return dossierService.getDossiersByCaseNum(caseNum);
    }

    /**
     * 查找一个案件中所有卷宗的操作记录
     * @param caseNum
     * @return
     */
    @RequestMapping("/his/all/{caseNum}")
    public List<DossierOperationRecord> getRecordsByCaseNum(@PathVariable String caseNum) {
        return dossierService.getDossierOperationRecordsByCaseNum(caseNum);
    }

    /**
     * 查找一个卷宗所有的操作记录
     * @param dossierId
     * @return
     */
    @RequestMapping("/his/dossier/{dossierId}")
    public List<DossierOperationRecord> getRecordsByDossierId(@PathVariable int dossierId) {
        return dossierService.getDossierOperationRecordsByDossierId(dossierId);
    }

    /**
     * 查找一个用户所有的卷宗操作记录
     * @param jobNum
     * @return
     */
    @RequestMapping("/his/dossier/{jobNum}")
    public List<DossierOperationRecord> getRecordsByDossierId(@PathVariable String jobNum) {
        return dossierService.getDossierOperationRecordsByJobNum(jobNum);
    }

    @RequestMapping("/add")
    public boolean add(Dossier dossier) {
        return dossierService.saveDossier(dossier);
    }

    /**
     * 通过id删除卷宗
     * @param dossierId
     * @return
     */
    @RequestMapping("/remove/{dossierId}")
    public boolean remove(@PathVariable long dossierId) {
        return dossierService.removeDossierById(dossierId);
    }

    /**
     * 通过id删除卷宗
     * @param req
     * @return
     */
    @RequestMapping("/addDirectory")
    public BaseResponse addDirectory(@RequestBody AddDirectoryReq req) {
        StringBuilder process = new StringBuilder(SystemSecurityUtils.getLoginUserName());
        try {
            dossierService.addDirectory(req.getCaseNum(), req.getDirectoryName());
        } catch (Exception e){
            process.append(" add directory to #").append(req.getCaseNum()).append(" occurred an error: ").append(e.getMessage());
            logger.error(process.toString());
            return new BaseResponse(Constants.CODE_FAIL, Constants.MSG_FAIL);
        }
        process.append(" added directory: ").append(req.getCaseNum()).append(" to #").append(req.getCaseNum());
        logger.info(process.toString());
        return new BaseResponse(Constants.CODE_SUCCESS, Constants.MSG_SUCCESS);
    }
}
