package edu.njusoftware.dossiermanagement.service.impl;

import edu.njusoftware.dossiermanagement.domain.Dossier;
import edu.njusoftware.dossiermanagement.service.DossierTextProcessor;
import edu.njusoftware.dossiermanagement.util.Constants;
import edu.njusoftware.dossiermanagement.util.FileEncodeUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

/**
 * 处理PDF文字的类
 */
@Service(Constants.FILE_TYPE_PDF)
public class PdfDossierTextProcessor implements DossierTextProcessor {
    @Override
    public void process(Dossier dossier) {
        File file = new File(dossier.getPath());
        String imagesFolder = file.getParent() + File.separator + "process";
        List<String> imagePaths = FileEncodeUtils.pdf2Images(dossier.getPath(), imagesFolder, 96);
        int total = imagePaths.size();
        for (int i = 0; i < total; i++) {

        }
    }

    private void dealImageFiles() {

    }
}
