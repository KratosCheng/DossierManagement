package edu.njusoftware.dossiermanagement.util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.schild.jave.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class FileEncodeUtils {
    private static final Logger logger = LoggerFactory.getLogger(FileEncodeUtils.class);

    /**
     *  第一个参数source表示要解码的源文件。
     *  第二个参数target是将要创建和编码的目标文件。
     * 请注意，此方法是阻塞的：只有在转码操作完成（或失败）后，该方法才会返回
     * @param
     * @return boolean
     */
//    public static boolean audio2PcmFiles(String sourcePath, String targetPath, float duration) {
//        File source = new File(sourcePath);
//        File target = new File(targetPath);
//        // Audio Attributes/音频编码属性
//        AudioAttributes audio = new AudioAttributes();
//        /*
//             * 它设置将用于音频流转码的编解码器的名称。您必须从当前Encoder实例的getAudioEncoders（）方法返回的列表中选择一个值。否则，
//             * 您可以传递AudioAttributes.DIRECT_STREAM_COPY特殊值，该值需要源文件中原始音频流的副本。
//             */
////            audio.setCodec("libmp3lame");
//        audio.setCodec("pcm_s16le");
//            /*
//             * 它设置新重新编码的音频流的比特率值。如果未设置比特率值，编码器将选择默认值。该值应以每秒位数表示。例如，如果你想要128 kb /
//             * s比特率，你应该调用setBitRate（new Integer（128000））。
//             */
//        audio.setBitRate(16000);
//        /* 它设置将在重新编码的音频流中使用的音频通道的数量（1 =单声道，2 =立体声）。如果未设置通道值，编码器将选择默认值。 */
//        audio.setChannels(1);
//            /*
//             * 它设置新重新编码的音频流的采样率。如果未设置采样率值，编码器将选择默认值。该值应以赫兹表示。例如，如果您想要类似CD的44100
//             * Hz采样率，则应调用setSamplingRate（new Integer（44100））。
//             */
//        audio.setSamplingRate(16000);
//        /* 可以调用此方法来改变音频流的音量。值256表示没有音量变化。因此，小于256的值是音量减小，而大于256的值将增加音频流的音量。 */
//        audio.setVolume(new Integer(256));
//
//        // Encoding attributes/编码属性
//        EncodingAttributes attrs = new EncodingAttributes();
//            /*
//             * 它设置将用于新编码文件的流容器的格式。给定参数表示格式名称。
//             * 编码格式名称有效且仅在它出现在正在使用的Encoder实例的getSupportedEncodingFormats（）方法返回的列表中时才受支持。
//             */
//        attrs.setFormat("s16le");
//        /* 它设置音频编码属性。如果从未调用过新的EncodingAttributes实例，或者给定参数为null，则编码文件中不会包含任何音频流 */
//        attrs.setAudioAttributes(audio);
//            /*
//             * 它为转码操作设置偏移量。源文件将从其开始的偏移秒开始重新编码。例如，如果您想剪切源文件的前五秒，
//             * 则应在传递给编码器的EncodingAttributes对象上调用setOffset（5）。
//             */
//        attrs.setOffset(offset);
//            /*
//             * 它设置转码操作的持续时间。只有源的持续时间秒才会在目标文件中重新编码。例如，如果您想从源中提取和转码30秒的一部分，
//             * 则应在传递给编码器的EncodingAttributes对象上调用setDuration（30）
//             */
//        attrs.setDuration(duration);
//        try {
//            // Encode/编码
//            Encoder encoder = new Encoder();
//            encoder.encode(new MultimediaObject(source), target, attrs, new ConvertProgressListener());
//            return true;
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            return false;
//        }
//    }

    public static List<String> audio2PcmFiles(String sourcePath, String targetFolder, float duration) {
        // Audio Attributes/音频编码属性
        AudioAttributes audio = new AudioAttributes();
        audio.setCodec("pcm_s16le");
        audio.setBitRate(16000);
        audio.setChannels(1);
        audio.setSamplingRate(16000);
        audio.setVolume(new Integer(256));
        // Encoding attributes/编码属性
        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setFormat("s16le");
        attrs.setAudioAttributes(audio);
        attrs.setDuration(duration);

        File sourceFile = new File(sourcePath);
        String pcmFileName = getPureName(sourceFile);

        MultimediaObject multimediaObject = new MultimediaObject(sourceFile);
        // Encode/编码
        Encoder encoder = new Encoder();

        List<String> pcmFilePaths = new LinkedList<>();
        float partStart = 0F;
        int part = 0;
        try {
            logger.debug("Start to encode file " + sourcePath + " to pcm files.");
            MultimediaInfo multimediaInfo = new MultimediaObject(sourceFile).getInfo();
            long totalDuration = multimediaInfo.getDuration() / 1000;  // 按秒为单位
            while (partStart < totalDuration) {
                String pcmFilePath = targetFolder + File.separator + pcmFileName + "_" + part + ".pcm";
                attrs.setOffset(partStart);
                encoder.encode(multimediaObject, new File(pcmFilePath), attrs, new ConvertProgressListener());
                partStart += duration;
                part++;
                pcmFilePaths.add(pcmFilePath);
            }
            logger.debug("Success to encode file " + sourcePath + " to pcm files, dest folder is " + targetFolder);
            return pcmFilePaths;
        } catch (Exception ex) {
            logger.error("Error to encode dossier file " + sourcePath);
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 获取文件名（不带格式后缀）
     * @param file
     * @return
     */
    private static String getPureName(File file) {
        return file.getName().substring(0, file.getName().lastIndexOf('.'));
    }

    public static class ConvertProgressListener implements EncoderProgressListener {
        /*编码器在分析源文件后调用此方法。该信息参数是实例 ws.schild.jave.MultimediaInfo类，它代表了有关源音频和视频流及其容器的信息。*/
        @Override
        public void sourceInfo(MultimediaInfo info) {
            // TODO Auto-generated method stub
        }

        /*每次完成编码操作的进度时，编码器调用该方法。所述permil参数是表示通过当前操作到达点的值和它的范围是从0（操作刚开始）到1000（操作完成）*/
        public void progress(int permil) {
            double progress = permil / 1000.00;
            System.out.println(progress);
        }

        /* 编码器调用该方法以通知关于代码转换操作的消息（通常消息是警告）。 */
        public void message(String message) {
            // TODO Auto-generated method stub
        }

    }

    /***
     * PDF文件转PNG/JPEG图片
     * @param pdfFilePath pdf完整路径
     * @param dstImgFolder 图片存放的文件夹
     * @param dpi 越大转换后越清晰，相对转换速度越慢,一般电脑默认96dpi
     */
    public static List<String> pdf2Images(String pdfFilePath, String dstImgFolder, int dpi) {
        File pdfFile = new File(pdfFilePath);
        String pdfName = getPureName(pdfFile);

        List<String> imagePaths = new LinkedList<>();
        try {
            if (createDirectory(dstImgFolder)) {
                PDDocument pdDocument = PDDocument.load(pdfFile);
                PDFRenderer renderer = new PDFRenderer(pdDocument);
                int pages = pdDocument.getNumberOfPages();// 获取PDF页数
                logger.debug("Start to separate and transform Pdf " + pdfFilePath + " to png images, number of pages is " + pages);
                for (int i = 0; i < pages; i++) {
                    String imgFilePath = dstImgFolder + File.separator + pdfName + "_" + i + ".png";
                    File dstFile = new File(imgFilePath);
//                    BufferedImage image = renderer.renderImageWithDPI(i, dpi);
                    BufferedImage image = renderer.renderImage(i);
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
     * 创建文件夹
     * @param folder
     * @return
     */
    private static boolean createDirectory(String folder) {
        File dir = new File(folder);
        if (dir.exists()) {
            return true;
        } else {
            logger.debug("Try to create folder " + folder);
            return dir.mkdirs();
        }
    }
}
