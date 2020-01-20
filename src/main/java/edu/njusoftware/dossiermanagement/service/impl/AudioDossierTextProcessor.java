package edu.njusoftware.dossiermanagement.service.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import edu.njusoftware.dossiermanagement.domain.Dossier;
import edu.njusoftware.dossiermanagement.service.DossierTextProcessor;
import edu.njusoftware.dossiermanagement.util.Constants;
import edu.njusoftware.dossiermanagement.util.FileEncodeUtils;
import edu.njusoftware.dossiermanagement.util.IATSpeechRecognizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaInfo;
import ws.schild.jave.MultimediaObject;

import java.io.File;
import java.util.List;

@Service(Constants.FILE_TYPE_AUDIO)
public class AudioDossierTextProcessor implements DossierTextProcessor {
    private static final Logger logger = LoggerFactory.getLogger(AudioDossierTextProcessor.class);

    // 单位为秒
    private static final long partDuration = 60;

    @Autowired
    private IATSpeechRecognizer recognizer;

    @Override
    public void process(Dossier dossier) {
        String sourcePath = dossier.getPath();
        // 将用户上传文件转码为pcm
        File sourceFile = new File(sourcePath);
        long duration = 0L;
        try {
            MultimediaInfo multimediaInfo = new MultimediaObject(sourceFile).getInfo();
            duration = multimediaInfo.getDuration();
        } catch (EncoderException e) {
            e.printStackTrace();
        }
        int total = (int) (duration / (1000 * partDuration) + 1);
        float partStart = 0F;
        int part = 0;
        // 切割长音频文件为60s，并编码为pcm格式
        while (part < total) {
            String pcmPartPath = sourceFile.getParent() + File.separator + dossier.getName() + "_" + part + ".pcm";
            if (FileEncodeUtils.convertingAudioToPcmFormat(sourcePath, pcmPartPath, partStart, partDuration)) {
                dealPcmFiles(pcmPartPath, part, dossier.getId());
            } else {
                logger.error("Encode dossier file " + dossier.getName() + " to pcm format raised an error in part" + part);
            }
            part++;
            partStart += partDuration;
        }
    }

    /**
     * 调用语音识别接口处理pcm文件
     * @param pcmFilePath
     */
    private void dealPcmFiles(String pcmFilePath, int part, long dossierId) {
        logger.debug("Start to recognize " + pcmFilePath + " of dossier:" + dossierId + " part" + part);
        List<String> resultStrings = null;
        try {
            resultStrings = recognizer.recognizePcmFileByte(pcmFilePath);
        } catch (IATSpeechRecognizer.SpeechResultException e) {
            e.printStackTrace();
        }

        // TODO: 2020/1/20
        if (resultStrings == null) {
            logger.error("Error to recognize file " + pcmFilePath);
            return;
        }

        StringBuilder content = new StringBuilder();
        StringBuilder timeInfo = new StringBuilder();

        JsonParser parser = new JsonParser();  //创建JSON解析器
        for (String resultString : resultStrings) {
            // 创建JsonObject对象，入参为待解析的json数据
            JsonObject result = (JsonObject) parser.parse(resultString);
            // 通过关键字ws得到最外层的json数组，听写结果
            JsonArray wsArray = result.get("ws").getAsJsonArray();
            for (int i = 0; i < wsArray.size(); i++) {
                JsonObject cwObject = wsArray.get(i).getAsJsonObject();
                // 计算当前中文词在整个音频文件中的起始位置
                long bg = (part * partDuration * 1000) + cwObject.get("bg").getAsInt();
                // 通过关键字cw得到第二个数组
                JsonArray cwArray = cwObject.get("cw").getAsJsonArray();
                parseContent(cwArray, bg, content, timeInfo);
            }
        }

        timeInfo.deleteCharAt(timeInfo.length() - 1);
    }

    /**
     * 合并中文词以及对应时间信息
     * @param cwArray
     * @param bg
     * @param content
     * @param timeInfo
     */
    private void parseContent(JsonArray cwArray, long bg, StringBuilder content, StringBuilder timeInfo) {
        for (int i = 0 ; i < cwArray.size(); i++) {
            // 找到需要的字符串
            String word = cwArray.get(i).getAsJsonObject().get("w").getAsString();
            content.append(word);
            int wordLength = word.length();
            for (int j = 0; j < wordLength; j++) {
                timeInfo.append(bg).append(Constants.TIME_INFO_SEPARATOR);
            }
        }
    }
}
