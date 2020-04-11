package edu.njusoftware.dossiermanagement.controller;

import edu.njusoftware.dossiermanagement.domain.Dossier;
import edu.njusoftware.dossiermanagement.domain.DossierContent;
import edu.njusoftware.dossiermanagement.domain.DossierOperationRecord;
import edu.njusoftware.dossiermanagement.domain.req.AddDirectoryReq;
import edu.njusoftware.dossiermanagement.domain.rsp.BaseResponse;
import edu.njusoftware.dossiermanagement.domain.rsp.CaseSearchResult;
import edu.njusoftware.dossiermanagement.service.IDossierContentService;
import edu.njusoftware.dossiermanagement.service.IDossierService;
import edu.njusoftware.dossiermanagement.util.Constants;
import edu.njusoftware.dossiermanagement.util.SecurityUtils;
import org.apache.catalina.connector.ClientAbortException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
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

    @Autowired
    private IDossierContentService dossierContentService;

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
        StringBuilder process = new StringBuilder(SecurityUtils.getLoginUserName());
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

    /**
     * 返回前端展示的卷宗音视频文件，断点续传
     * @param response
     * @param dossierId
     */
    @RequestMapping("/common/getFile/{dossierId}")
    public void getFile(HttpServletRequest request, HttpServletResponse response, @PathVariable long dossierId) {
        Dossier dossier = dossierService.getDossier(dossierId);

        File file = new File(dossier.getPath());

        String range=request.getHeader("Range");
        //开始下载位置
        long startByte = 0;
        //结束下载位置
        long endByte = file.length() - 1;
        //有range的话
        if (range != null && range.contains("bytes=") && range.contains("-")) {
            range = range.substring(range.lastIndexOf("=") + 1).trim();
            String ranges[] = range.split("-");
            try {
                //判断range的类型
                if (ranges.length == 1) {
                    //类型一：bytes=-2343
                    if (range.startsWith("-")) {
                        endByte = Long.parseLong(ranges[0]);
                    }
                    //类型二：bytes=2343-
                    else if (range.endsWith("-")) {
                        startByte = Long.parseLong(ranges[0]);
                    }
                }
                //类型三：bytes=22-2343
                else if (ranges.length == 2) {
                    startByte = Long.parseLong(ranges[0]);
                    endByte = Long.parseLong(ranges[1]);
                }
            } catch (NumberFormatException e) {
                startByte = 0;
                endByte = file.length() - 1;
            }
        }
        //要下载的长度
        long contentLength = endByte - startByte + 1;
        //文件名
        String fileName = dossier.getName();
        //文件类型
        String contentType = request.getServletContext().getMimeType(fileName);
        //各种响应头设置
        //参考资料：https://www.ibm.com/developerworks/cn/java/joy-down/index.html
        //坑爹地方一：看代码
        response.setHeader("Accept-Ranges", "bytes");
        //坑爹地方二：http状态码要为206
        response.setStatus(206);
        response.setContentType(contentType);
        response.setHeader("Content-Type", contentType);
        //这里文件名换你想要的，inline表示浏览器直接实用（我方便测试用的）
        //参考资料：http://hw1287789687.iteye.com/blog/2188500
        // response.setHeader("Content-Disposition", "inline;filename=test.mp3");
        response.setHeader("Content-Length", String.valueOf(contentLength));
        //坑爹地方三：Content-Range，格式为
        // [要下载的开始位置]-[结束位置]/[文件总大小]
        response.setHeader("Content-Range", "bytes " + startByte + "-" + endByte + "/" + file.length());
        BufferedOutputStream outputStream = null;
        RandomAccessFile randomAccessFile = null;
        //已传送数据大小
        long transmitted = 0;
        try {
            randomAccessFile = new RandomAccessFile(file, "r");
            outputStream = new BufferedOutputStream(response.getOutputStream());
            byte[] buff = new byte[4096];
            int len = 0;
            randomAccessFile.seek(startByte);
            //坑爹地方四：判断是否到了最后不足4096（buff的length）个byte这个逻辑（(transmitted + len) <= contentLength）要放前面！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！
            //不然会会先读取randomAccessFile，造成后面读取位置出错，找了一天才发现问题所在
            while ((transmitted + len) <= contentLength && (len = randomAccessFile.read(buff)) != -1) {
                outputStream.write(buff, 0, len);
                transmitted += len;
                //停一下，方便测试，用的时候删了就行了
                Thread.sleep(10);
            }
            //处理不足buff.length部分
            if (transmitted < contentLength) {
                len = randomAccessFile.read(buff, 0, (int) (contentLength - transmitted));
                outputStream.write(buff, 0, len);
                transmitted += len;
            }
            outputStream.flush();
            response.flushBuffer();
            randomAccessFile.close();
            System.out.println(fileName + " 下载完毕：" + startByte + "-" + endByte + "：" + transmitted);
        } catch (ClientAbortException e) {
            System.out.println(fileName +  " 用户停止下载：" + startByte + "-" + endByte + "：" + transmitted);
            //捕获此异常表示拥护停止下载
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                if (randomAccessFile != null) {
                    randomAccessFile.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 返回前端展示的卷宗PDF文件
     * @param response
     * @param dossierId
     */
    @RequestMapping("/common/getPDFFile/{dossierId}")
    public void getPDFFile(HttpServletResponse response, @PathVariable long dossierId) {
        Dossier dossier = dossierService.getDossier(dossierId);
        response.setContentType(fileTypeMap.get(dossier.getFileType()));
        FileInputStream in;
        OutputStream out;
        try {
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
    @RequestMapping("/common/updateCurrentDossier/{dossierId}")
    public String updateCurrentDossier(Model model, @PathVariable long dossierId) {
        model.addAttribute("currentDossier", dossierService.getDossier(dossierId));
        // 设置显示第一部分的文本内容
        model.addAttribute("dossierContent", dossierContentService.getDossierContent(dossierId, 0));
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
        logger.info(SecurityUtils.getLoginUserName() + " tries to upload dossier: " + dossier.getName() +
                " in #" + dossier.getCaseNum() + "/" + dossier.getDirectory());
        String[] fileNameParts = Objects.requireNonNull(file.getOriginalFilename()).split("\\.");
        if (StringUtils.isEmpty(dossier.getName())) {
            dossier.setName(fileNameParts[0]);
        }

        String filePath = Constants.DOSSIER_BASE_DIRECTORY + dossier.getCaseNum() + "/" +
                dossier.getDirectory() + "/" + dossier.getName() + "/" + dossier.getName() + "." + fileNameParts[1];

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
        dossier.setUploadUser(SecurityUtils.getLoginUserName());
        dossier.setUploadTime(new Date());

        if (!dossierService.saveDossier(dossier)) {
            return new BaseResponse(1, "上传失败！Error to store dossier!");
        }
        dossierService.processDossierContent(dossier);
        return new BaseResponse(0, "上传卷宗成功！");

    }

    /**
     * 修正卷宗文本内容
     * @param model
     * @param dossierContent
     * @return
     */
    @RequestMapping(value = "/rectifyContent")
    public String rectifyContent(Model model, DossierContent dossierContent) {
        // 消除空格
        DossierContent newDossierContent = dossierContentService.rectifyDossierContent(
                dossierContent.getDossierId(), dossierContent.getPart(), dossierContent.getContent().replace(" ", ""));
        model.addAttribute("dossierContent", newDossierContent);
        return "casePage::#div-text-container";
    }

    /**
     * 获取卷宗文本内容的历史操作记录
     * @param model
     * @param dossierId
     * @param part
     * @return
     */
    @RequestMapping(value = "/contentHis")
    public String getContentHis(Model model, long dossierId, int part) {
        model.addAttribute("contentHisList", dossierContentService.getContentHis(dossierId, part));
        return "casePage::#text_his_dialog";
    }

    /**
     * 重置卷宗文本内容
     * @param operationRecordId
     * @return
     */
    @RequestMapping(value = "/resetContent/{operationRecordId}")
    public String resetContent(Model model, @PathVariable long operationRecordId) {
        DossierContent newDossierContent = dossierContentService.resetDossierContent(operationRecordId);
        model.addAttribute("dossierContent", newDossierContent);
        return "casePage::#div-text-container";
    }

    /**
     * 在案件中查找关键字
     * @param model
     * @param caseNum
     * @param keyword
     * @return
     */
    @RequestMapping(value = "/caseSearch")
    public String caseSearch(Model model, String caseNum, String keyword) {
        List<CaseSearchResult> caseSearchResults = dossierService.caseSearch(caseNum, keyword);
        model.addAttribute("caseSearchResults", caseSearchResults);
        return "casePage::#search_result_dialog";
    }

    /**
     * 在案件中查找关键字
     * @param model
     * @param dossierId
     * @param keyword
     * @return
     */
    @RequestMapping(value = "/search")
    public String dossierSearch(Model model, long dossierId, String keyword) {
        List<CaseSearchResult> caseSearchResults = dossierService.search(dossierId, keyword);
        model.addAttribute("caseSearchResults", caseSearchResults);
        return "casePage::#search_result_dialog";
    }
}
