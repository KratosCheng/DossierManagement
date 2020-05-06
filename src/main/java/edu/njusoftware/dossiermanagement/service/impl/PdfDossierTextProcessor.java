package edu.njusoftware.dossiermanagement.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import edu.njusoftware.dossiermanagement.domain.Dossier;
import edu.njusoftware.dossiermanagement.domain.DossierContent;
import edu.njusoftware.dossiermanagement.domain.PDFLine;
import edu.njusoftware.dossiermanagement.repository.DossierContentRepository;
import edu.njusoftware.dossiermanagement.service.DossierTextProcessor;
import edu.njusoftware.dossiermanagement.service.OperationRecordService;
import edu.njusoftware.dossiermanagement.util.Constants;
import edu.njusoftware.dossiermanagement.util.PDFFileEncodeUtils;
import edu.njusoftware.dossiermanagement.util.PDFRecognizer;
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

//        List<String> imagePaths = new ArrayList<>();
//        imagePaths.add(imagesFolder + File.separator + "(2014)滨刑初字第0079号 受贿罪139页_0.png");

        List<String> imagePaths = PDFFileEncodeUtils.pdf2Images(dossier.getPath(), imagesFolder, 300);
        int total = imagePaths.size();
        // 备份pdf文件
        backUpFile(pdfPath);
        logger.debug("Start to process png files of pdf: " + pdfPath);
        for (int i = 0; i < total; i++) {
            processSinglePage(imagePaths.get(i), pdfPath, dossier, i);
        }
    }

    /**
     * 处理pdf中某页
     * @param imgPath
     * @param pdfPath
     * @param dossier
     * @param pageIndex
     */
    private void processSinglePage(String imgPath, String pdfPath, Dossier dossier, int pageIndex) {
        String result = pdfRecognizer.recognizePicture(imgPath);
        StringBuilder locationInfo = new StringBuilder();   // 记录位置信息
        StringBuilder content = new StringBuilder();    // 该页纯文本信息
        List<PDFLine> pdfLines = dealOCRResult(result, locationInfo, content);
        try {
            // 设置扫描件第i页的文本层
            PDFFileEncodeUtils.setPageTextContent(pdfPath, pdfLines, pageIndex);
        } catch (IOException e) {
            e.printStackTrace();
        }
        DossierContent dossierContent = new DossierContent(dossier.getId(),
                dossier.getFileType(), pageIndex, locationInfo.substring(1), content.toString(), result);
        dossierContentRepository.save(dossierContent);
        operationRecordService.saveContentRecognitionOperationRecord(dossier.getCaseNum(), dossier.getId(),
                dossier.getName(), pageIndex, content.toString());
    }

    /**
     * 处理单张图片的识别结果
     * @param result
     * @param locationInfo
     * @param pageContent
     */
    private List<PDFLine> dealOCRResult(String result, StringBuilder locationInfo, StringBuilder pageContent) {
        JSONObject object = JSONObject.parseObject(result);
        // 获取图片高度
        int picHeight = object.getJSONObject("meta").getIntValue("height");
        int picWidth = object.getJSONObject("meta").getIntValue("width");
        // 识别结果中行的数组
        JSONArray lines = object.getJSONArray("lines");
        int numOfLines = lines.size();
        List<PDFLine> pdfLines = new ArrayList<>(numOfLines);
        for (int i = 0; i < numOfLines; i++) {
            // 行中字符的数组
            JSONArray line = lines.getJSONArray(i);
            JSONObject firstChar = line.getJSONObject(0);
            // 计算此行左下角到页面左下角y轴方向的位移，ocr识别结果为图片左上角到页面左上角的位移
            float lineTy = divideAndKeep3ValidNums(picHeight - firstChar.getIntValue("y") - firstChar.getIntValue("h"), picHeight);
            int numOfChars = line.size(), avgHeight = 0, validCount = 0;
            // 存储这一行的文本
            StringBuilder text = new StringBuilder();
            for (int j = 0; j < numOfChars; j++) {
                JSONObject character = line.getJSONObject(j);
                text.append(character.getString("c"));
                String location = divideAndKeep3ValidNums(character.getIntValue("x"), picWidth) + "," +
                        divideAndKeep3ValidNums(character.getIntValue("y"), picWidth) + "," +
                        divideAndKeep3ValidNums(character.getIntValue("h"), picWidth) + "," +
                        divideAndKeep3ValidNums(character.getIntValue("w"), picWidth);
                locationInfo.append("_").append(location);
                int curHeight = character.getIntValue("h");
                if (curHeight < 200) {
                    avgHeight += curHeight;
                    validCount++;
                }
            }
            pageContent.append(text);
            if (validCount > 0) {
                avgHeight /= validCount;
            }
            System.out.println(text + " " + avgHeight + " valid count:" + validCount);
            pdfLines.add(new PDFLine(text.toString(), divideAndKeep3ValidNums(firstChar.getIntValue("x"), picWidth),
                    lineTy, estimateFontSize(avgHeight), 200, 0, 0));
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

        String[] path = filePath.split("\\.");
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
        if (charHeight > 75) {
            return 28;
        } else if (charHeight > 40) {
            return 16;
        } else {
            return 12;
        }
    }

    /**
     * 保留三位小数除
     * @param num
     * @return
     */
    private float divideAndKeep3ValidNums(float num, float divide) {
        return (float) Math.round(num/divide * 1000) / 1000;
    }
}
