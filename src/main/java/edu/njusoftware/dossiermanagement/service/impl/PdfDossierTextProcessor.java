package edu.njusoftware.dossiermanagement.service.impl;

import edu.njusoftware.dossiermanagement.domain.Dossier;
import edu.njusoftware.dossiermanagement.service.DossierTextProcessor;
import edu.njusoftware.dossiermanagement.util.Constants;
import edu.njusoftware.dossiermanagement.util.FileEncodeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

/**
 * 处理PDF文字的类
 */
@Service(Constants.FILE_TYPE_PDF)
public class PdfDossierTextProcessor implements DossierTextProcessor {

    private static final Logger logger = LoggerFactory.getLogger(AudioDossierTextProcessor.class);

    @Value("${ocr.url}")
    private String ocrUrl;

    @Override
    public void process(Dossier dossier) {
        File file = new File(dossier.getPath());
        String imagesFolder = file.getParent() + File.separator + "process";
        List<String> imagePaths = FileEncodeUtils.pdf2Images(dossier.getPath(), imagesFolder, 96);
        int total = imagePaths.size();
        logger.debug("Start to process png files, the ocr interface url: " + ocrUrl);
        for (int i = 0; i < total; i++) {
            String result = dealImageFiles(imagePaths.get(i));
        }
    }

    private String dealImageFiles(String imagePath) {
        logger.debug("Call the ocr interface to recognize file:  " + imagePath);

        return null;
    }
}
