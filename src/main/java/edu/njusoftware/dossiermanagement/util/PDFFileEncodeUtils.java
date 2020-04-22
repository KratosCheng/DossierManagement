package edu.njusoftware.dossiermanagement.util;

import edu.njusoftware.dossiermanagement.domain.PDFLine;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.util.Matrix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class PDFFileEncodeUtils {
    private static final Logger logger = LoggerFactory.getLogger(AudioFileEncodeUtils.class);

    /***
     * PDF文件转PNG/JPEG图片
     * @param pdfFilePath pdf完整路径
     * @param dstImgFolder 图片存放的文件夹
     * @param dpi 越大转换后越清晰，相对转换速度越慢,一般电脑默认96dpi
     */
    public static List<String> pdf2Images(String pdfFilePath, String dstImgFolder, int dpi) {
        File pdfFile = new File(pdfFilePath);
        String pdfName = AudioFileEncodeUtils.getPureName(pdfFile);

        List<String> imagePaths = new LinkedList<>();
        try {
            if (AudioFileEncodeUtils.createDirectory(dstImgFolder)) {
                PDDocument pdDocument = PDDocument.load(pdfFile);
                PDFRenderer renderer = new PDFRenderer(pdDocument);
                int pages = pdDocument.getNumberOfPages();// 获取PDF页数
                logger.debug("Start to separate and transform Pdf " + pdfFilePath + " to png images, number of pages is " + pages);
                for (int i = 0; i < pages; i++) {
                    String imgFilePath = dstImgFolder + File.separator + pdfName + "_" + i + ".png";
                    File dstFile = new File(imgFilePath);
                    BufferedImage image = renderer.renderImageWithDPI(i, dpi);
                    ImageIO.write(image, "png", dstFile); // PNG
                    imagePaths.add(imgFilePath);
                }
                logger.debug("Success to separate and transform Pdf " + pdfFilePath + " to png images, destination folder is  " + dstImgFolder);
            } else {
                logger.error("Error to create folder " + dstImgFolder);
            }
        } catch (IOException e) {
            logger.error("Error to separate and transform Pdf " + pdfFilePath + " to png images!");
            e.printStackTrace();
            return null;
        }
        return imagePaths;
    }

    /**
     * 设置pdf文件某页的文本层
     * @param filePath
     * @param lines
     * @param pageIndex
     * @return
     * @throws IOException
     */
    public static PDPage setPageTextContent(String filePath, List<PDFLine> lines, int pageIndex) throws IOException {
        File file = new File(filePath);
        PDDocument doc = PDDocument.load(file);
        doc.setAllSecurityToBeRemoved(true);
        PDPage page = doc.getPage(pageIndex);
        float pageWidth = page.getCropBox().getWidth();
        float pageHeight = page.getCropBox().getHeight();
        PDPageContentStream cs = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.APPEND, true, true);
        Resource resource = new ClassPathResource("static/fonts/simfang.ttf");
        File fontFile = resource.getFile();
        PDFont font = PDType0Font.load(doc, fontFile);
        PDExtendedGraphicsState r0 = new PDExtendedGraphicsState();
        // 透明度
        r0.setNonStrokingAlphaConstant(0f);
        r0.setAlphaSourceFlag(true);
        cs.setGraphicsStateParameters(r0);
        for (PDFLine line : lines) {
//            cs.setNonStrokingColor(line.getR(), line.getG(), line.getB());
            cs.setNonStrokingColor(200, 0, 0);
            cs.setFont(font, line.getFontSize());
            cs.beginText();
            // 获取平移实例，tx - 坐标在 X 轴方向上平移的距离，ty - 坐标在 Y 轴方向上平移的距离
            cs.setTextMatrix(Matrix.getTranslateInstance(line.getTx() * pageWidth, line.getTy() * pageHeight));
            cs.showText(line.getText());
            cs.endText();
        }
        cs.close();
        doc.save(file);
        return page;
    }

    /**
     * 用于获取一页pdf中的一些字符图片
     * @param pdfFilePath
     * @param targetFolder
     * @param positions
     * @return
     */
    public static List<String> getCharImages(String pdfFilePath, int page, String targetFolder, List<String> positions) {
        File pdfFile = new File(pdfFilePath);
        String pdfName = AudioFileEncodeUtils.getPureName(pdfFile);

        List<String> imagePaths = new LinkedList<>();
        try {
            if (!AudioFileEncodeUtils.createDirectory(targetFolder)) {
                return null;
            }
            PDDocument pdDocument = PDDocument.load(pdfFile);
            PDFRenderer renderer = new PDFRenderer(pdDocument);
            int count = positions.size();
            BufferedImage pageImage = renderer.renderImage(page);
            String imagePathPrefix = targetFolder + File.separator + pdfName + "_" + page + "_";
            logger.debug("Start to separate " + pdfFilePath + " page " + page + " to png images, count is " + count);
            for (int i = 0; i < count; i++) {
                String[] indexs = positions.get(i).split("_");
                String imgFilePath = imagePathPrefix + i + ".png";
                File dstFile = new File(imgFilePath);
                BufferedImage charImage = pageImage.getSubimage(Integer.parseInt(indexs[0]),
                        Integer.parseInt(indexs[1]), Integer.parseInt(indexs[2]), Integer.parseInt(indexs[3]));
                ImageIO.write(charImage, "png", dstFile); // PNG
                imagePaths.add(imgFilePath);
            }
            logger.debug("Success to separate " + pdfFilePath + " page " + page + " to png images, destination folder is  " + targetFolder);
        } catch (IOException e) {
            logger.error("Error to separate Pdf "  + " page " + page + pdfFilePath + " to png images!");
            e.printStackTrace();
            return null;
        }
        return imagePaths;
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
