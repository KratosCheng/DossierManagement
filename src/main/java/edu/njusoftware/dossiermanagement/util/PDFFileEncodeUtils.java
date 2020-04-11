package edu.njusoftware.dossiermanagement.util;

import edu.njusoftware.dossiermanagement.domain.PDFLine;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState;
import org.apache.pdfbox.util.Matrix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class PDFFileEncodeUtils {
    private static final Logger logger = LoggerFactory.getLogger(FileEncodeUtils.class);

    public static PDPage alterPageContent(String filePath, List<PDFLine> lines, int pageIndex) throws IOException {
        File file = new File(filePath);
        PDDocument doc = PDDocument.load(file);
        doc.setAllSecurityToBeRemoved(true);
        PDPage page = doc.getPage(pageIndex);
//        PDPageContentStream cs = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.APPEND, true, true);
        PDPageContentStream cs = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.APPEND, true, true);
        Resource resource = new ClassPathResource("static/fonts/simfang.ttf");
        File fontFile = resource.getFile();
        PDFont font = PDType0Font.load(doc, fontFile);
        PDExtendedGraphicsState r0 = new PDExtendedGraphicsState();
//        // 透明度
        r0.setNonStrokingAlphaConstant(0f);
        r0.setAlphaSourceFlag(true);
        cs.setGraphicsStateParameters(r0);
//        cs.setNonStrokingColor(lines,0,0);//Red
        for (PDFLine line : lines) {
            cs.setNonStrokingColor(line.getR(),line.getG(),line.getB());
            cs.setFont(font, line.getFontSize());
            cs.beginText();
            // 获取平移实例，tx - 坐标在 X 轴方向上平移的距离，ty - 坐标在 Y 轴方向上平移的距离
            cs.setTextMatrix(Matrix.getTranslateInstance(line.getTx(),line.getTy()));
            cs.showText(line.getText());
            cs.endText();
        }
        cs.close();
        doc.save(file);
        return page;
    }

    public static void test(String filePath) throws IOException {
        File file = new File(filePath);
        PDDocument doc = PDDocument.load(file);

        //Creating a PDF Document
        PDPage page = doc.getPage(1);

        PDPageContentStream contentStream = new PDPageContentStream(doc, page);

        //Begin the Content stream
        contentStream.beginText();

        Resource resource = new ClassPathResource("static/fonts/simfang.ttf");
        File fontFile = resource.getFile();
        PDFont font = PDType0Font.load(doc, fontFile);
        //Setting the font to the Content stream
        contentStream.setFont( font, 16 );

        //Setting the leading
        contentStream.setLeading(14.5f);

        //Setting the position for the line
        contentStream.newLineAtOffset(25, 725);

        String text1 = "哈哈哈哈第一行的就是这个";
        String text2 = "她爱i啊速度粉丝u阿苏";

        //Adding text in the form of string
        contentStream.showText(text1);
        contentStream.newLineAtOffset(50, 700);
        contentStream.showText(text2);
        //Ending the content stream
        contentStream.endText();

        System.out.println("Content added");

        //Closing the content stream
        contentStream.close();

        //Saving the document
        doc.save(file);

        //Closing the document
        doc.close();
    }
}
