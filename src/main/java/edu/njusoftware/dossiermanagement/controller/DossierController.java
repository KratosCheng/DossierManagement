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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.*;

@Controller
@RequestMapping("/dossier")
public class DossierController {
    private static final Logger logger = LoggerFactory.getLogger(DossierController.class);

    private static final Map<String, String> fileTypeMap = new HashMap<>(10);

    static {
        fileTypeMap.put("pdf", "application/pdf");
        fileTypeMap.put("video", "video/mp4");
        fileTypeMap.put("audio", "audio/mp3");
    }

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
    @ResponseBody
    public BaseResponse addDirectory(@RequestBody AddDirectoryReq req) {
        StringBuilder process = new StringBuilder(SystemSecurityUtils.getLoginUserName());
        try {
            dossierService.addDirectory(req.getCaseNum(), req.getDirectoryName());
        } catch (Exception e){
            process.append(" add directory to #").append(req.getCaseNum()).append(" occurred an error: ").append(e.getMessage());
            logger.error(process.toString());
            return new BaseResponse(Constants.CODE_FAIL, Constants.MSG_FAIL);
        }
        process.append(" added directory: ").append(req.getDirectoryName()).append(" to #").append(req.getCaseNum());
        logger.info(process.toString());
        return new BaseResponse(Constants.CODE_SUCCESS, Constants.MSG_SUCCESS);
    }

//    @RequestMapping("/getFile/{dossierId}")
//    public ResponseEntity<FileSystemResource> getFile(@PathVariable long dossierId) {
////        Dossier dossier = dossierService.getDossier(dossierId);
////        File file = new File(dossier.getPath());
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
////        headers.add("Content-Disposition", "attachment; filename=" + dossier.getName());
//        headers.add("Pragma", "no-cache");
//        headers.add("Expires", "0");
//        headers.add("Last-Modified", new Date().toString());
//        headers.add("ETag", String.valueOf(System.currentTimeMillis()));
//
//        File file = new File("C:/Users/Kratos/Desktop/dossier/test/卷宗扫描件/requirements.pdf");
//        headers.add("Content-Disposition", "attachment; filename=\"requirements.pdf\"");
//
//        return ResponseEntity
//                .ok()
//                .headers(headers)
//                .contentLength(file.length())
//                .contentType(MediaType.parseMediaType("application/octet-stream"))
//                .body(new FileSystemResource(file));
//    }

    /**
     * 返回前端展示的卷宗多媒体文件
     * @param request
     * @param response
     * @param session
     * @param dossierId
     */
    @RequestMapping("/getFile/{dossierId}")
    public void getFile(HttpServletRequest request, HttpServletResponse response, HttpSession session, @PathVariable long dossierId) {
        Dossier dossier = dossierService.getDossier(dossierId);
        response.setContentType(fileTypeMap.get(dossier.getFileType()));
        FileInputStream in;
        OutputStream out;
        try {
//            in = new FileInputStream(new File("C:/Users/Kratos/Desktop/dossier/test/音频证据/荣耀-35.mp3"));
            in = new FileInputStream(new File(dossier.getPath()));
            in = new FileInputStream(new File(dossier.getPath()));
            out = response.getOutputStream();
            byte[] b = new byte[512];
            while ((in.read(b)) != -1) {
                out.write(b);
            }
            out.flush();
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 局部刷新卷宗文件展示
     * @param dossierId
     * @return
     */
    @RequestMapping("/updateCurrentDossier/{dossierId}")
    public String updateCurrentDossier(Model model, @PathVariable long dossierId) {
        model.addAttribute("currentDossier", dossierService.getDossier(dossierId));
        return "casePage::#div-media-container";
    }

    /**
     * 新增案件
     * @param dossier
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse addDossier(@RequestParam(value = "file") MultipartFile file, Dossier dossier) {
        logger.info(SystemSecurityUtils.getLoginUserName() + " tries to upload dossier: " + dossier.getName() +
                " in #" + dossier.getCaseNum() + "/" + dossier.getDirectory());
        String[] fileNameParts = Objects.requireNonNull(file.getOriginalFilename()).split("\\.");
        if (StringUtils.isEmpty(dossier.getName())) {
            dossier.setName(fileNameParts[0]);
        }

        String filePath = Constants.DOSSIER_BASE_DIRECTORY + dossier.getCaseNum() + "/" + dossier.getDirectory() + "/" + dossier.getName() + "." + fileNameParts[1];

        try {
            File destFile = new File(filePath);
            destFile.getParentFile().mkdirs();
            file.transferTo(destFile);
            logger.info("Store uploaded dossier: " + dossier.getName() + " in #" + dossier.getCaseNum() + "/" +
                    dossier.getDirectory() + " at " + destFile.getPath());
        } catch (IOException e) {
            e.printStackTrace();
            return new BaseResponse(1, "上传失败," + e.getMessage());
        }

        dossier.setPath(filePath);
        dossier.setUploadUser(SystemSecurityUtils.getLoginUserName());
        dossier.setUploadTime(new Date());
        return dossierService.saveDossier(dossier) ? new BaseResponse(0, "上传卷宗成功！") :
                new BaseResponse(1, "上传失败！Error to store dossier!");
    }
}
