package edu.njusoftware.dossiermanagement.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonArray;
import edu.njusoftware.dossiermanagement.domain.Dossier;
import edu.njusoftware.dossiermanagement.domain.DossierContent;
import edu.njusoftware.dossiermanagement.domain.PDFLine;
import edu.njusoftware.dossiermanagement.repository.DossierContentRepository;
import edu.njusoftware.dossiermanagement.service.DossierTextProcessor;
import edu.njusoftware.dossiermanagement.service.OperationRecordService;
import edu.njusoftware.dossiermanagement.util.Constants;
import edu.njusoftware.dossiermanagement.util.PDFFileEncodeUtils;
import edu.njusoftware.dossiermanagement.util.PDFRecognizer;
import netscape.javascript.JSObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 处理PDF文字的类
 */
@Service(Constants.FILE_TYPE_PDF)
public class PdfDossierTextProcessor implements DossierTextProcessor {

    private static final Logger logger = LoggerFactory.getLogger(PdfDossierTextProcessor.class);

    @Autowired
    private PDFRecognizer pdfRecognizer;

    @Autowired
    private DossierContentRepository dossierContentRepository;

    @Autowired
    private OperationRecordService operationRecordService;

    @Override
    public void process(Dossier dossier) {
        String pdfPath = dossier.getPath();
        File file = new File(pdfPath);
        String imagesFolder = file.getParent() + File.separator + "process";
        List<String> imagePaths = PDFFileEncodeUtils.pdf2Images(dossier.getPath(), imagesFolder, 96);
        int total = imagePaths.size();
        // 备份pdf文件
        backUpFile(pdfPath);
        logger.debug("Start to process png files." );
        for (int i = 0; i < total; i++) {
            String result = pdfRecognizer.recognizePicture(imagePaths.get(i));
            List<PDFLine> pdfLines = dealOCRResult(result);
            try {
                // 设置扫描件第i页的文本层
                PDFFileEncodeUtils.setPageTextContent(pdfPath, pdfLines, i);
            } catch (IOException e) {
                e.printStackTrace();
            }
            DossierContent dossierContent = new DossierContent();
            dossierContentRepository.save(dossierContent);
//            operationRecordService.saveContentRecognitionOperationRecord(caseNum, dossierId, dossierName, part, content.toString());
        }
    }

    /**
     * 处理单张图片的识别结果
     * @param result
     */
    private List<PDFLine> dealOCRResult(String result) {
        JSONObject object = JSONObject.parseObject(result);
        // 获取图片高度
        int picHeight = object.getJSONObject("meta").getIntValue("height");
        // 识别结果中行的数组
        JSONArray lines = object.getJSONArray("lines");
        int numOfLines = lines.size();
        List<PDFLine> pdfLines = new ArrayList<>(numOfLines);
        for (int i = 0; i < numOfLines; i++) {
            // 行中字符的数组
            JSONArray line = lines.getJSONArray(i);
            JSONObject firstChar = line.getJSONObject(0);
            // 计算此行到页面左下角y轴方向的位移
            int lineTy = picHeight - firstChar.getIntValue("y") - firstChar.getIntValue("h");
            int numOfChars = line.size();
            // 存储这一行的文本
            StringBuilder stringBuilder = new StringBuilder(firstChar.getString("c"));
            for (int j = 1; j < numOfChars; j++) {
                JSONObject character = line.getJSONObject(j);
                stringBuilder.append(character.getString("c"));
            }
            pdfLines.add(new PDFLine(stringBuilder.toString(), firstChar.getIntValue("x"), lineTy,
                    estimateFontSize(firstChar.getIntValue("h"))));
        }
        return pdfLines;
    }

    /**
     * 备份pdf文件
     * @param filePath
     */
    private void backUpFile(String filePath) {
        File srcFile = new File(filePath);
        if(!srcFile.exists()){
            System.out.println("模板文件不存在");
            return ;
        }

        String[] path = filePath.split(".");
        //创建备份文件
        File backUpFile = new File(path[0] + "_back." + path[1]);
        try {
            if(backUpFile.createNewFile()){
                //创建备份文件成功，进行文件复制
                InputStream src = new BufferedInputStream(new FileInputStream(srcFile));
                OutputStream dest = new BufferedOutputStream(new FileOutputStream(backUpFile));
                byte[] trans = new byte[1024];
                int count = -1;

                while((count = src.read(trans)) != -1){
                    dest.write(trans, 0, count);
                }
                dest.flush();
                src.close();
                dest.close();
            }
        } catch (Exception e) {
            System.out.println("备份文件失败");
        }
    }

    /**
     * 根据字符高度估算字体大小
     * @param charHeight
     * @return
     */
    private int estimateFontSize(int charHeight) {
        //Todo
        return 16;
    }
}
