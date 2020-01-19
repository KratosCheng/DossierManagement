package edu.njusoftware.dossiermanagement.util;

import com.baidu.aip.speech.AipSpeech;
import com.baidu.aip.speech.TtsResponse;
import com.baidu.aip.util.Util;
import com.iflytek.cloud.speech.SpeechConstant;
import com.iflytek.cloud.speech.SpeechRecognizer;
import com.sun.media.sound.SoftAudioProcessor;
import edu.njusoftware.dossiermanagement.controller.UserController;
import javazoom.spi.mpeg.sampled.file.MpegAudioFileReader;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tritonus.share.sampled.AudioUtils;
import org.tritonus.share.sampled.file.AudioOutputStream;
import org.tritonus.share.sampled.file.TAudioFileFormat;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

/**
 * 百度语音工具类
 */
public class BaiduSpeechUtils {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private static final String APP_ID = "15948020";
    //你创建的App应用的API Key
    private static final String API_KEY = "sPbOLUBOGq5U3WR9fpHOvtcP";
    //你创建的App应用的Secret Key
    private static final String SECRET_KEY = "ZcyHqnuXHS41vC3SytBwwAPiSAkGW3qa";

    /**
     * 语音识别，识别结果为纯文本，无时间信息，故采用讯飞
     * @param path 路径
     * @return
     */
    public static String aSpeechRecognition(String path) throws IOException {
        // 初始化一个AipSpeech
        AipSpeech client = new AipSpeech(APP_ID, API_KEY, SECRET_KEY);

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

        JSONObject asrRes2 = client.asr(path, "pcm", 16000, null);
        return asrRes2.toString();
    }


    /**
     * MP3转换PCM文件方法
     *
     * @param mp3filepath 原始文件路径
     * @param pcmfilepath 转换文件的保存路径
     * @return
     * @throws Exception
     */
    public static boolean convertMP32Pcm(String mp3filepath, String pcmfilepath){
        try {
            //获取文件的音频流，pcm的格式
            AudioInputStream audioInputStream = getPcmAudioInputStream(mp3filepath);
            //将音频转化为  pcm的格式保存下来
            AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, new File(pcmfilepath));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获得pcm文件的音频流
     * @param mp3filepath
     * @return
     */
    private static AudioInputStream getPcmAudioInputStream(String mp3filepath) {
        File mp3 = new File(mp3filepath);
        AudioInputStream audioInputStream = null;
        AudioFormat targetFormat = null;
        try {
            AudioInputStream in = null;
            MpegAudioFileReader mp = new MpegAudioFileReader();
            in = mp.getAudioInputStream(mp3);
            AudioFormat baseFormat = in.getFormat();
            targetFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16,
                    baseFormat.getChannels(), baseFormat.getChannels()*2, baseFormat.getSampleRate(), false);
            audioInputStream = AudioSystem.getAudioInputStream(targetFormat, in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return audioInputStream;
    }


}