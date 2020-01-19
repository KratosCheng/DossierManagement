package edu.njusoftware.dossiermanagement.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.iflytek.cloud.speech.*;
import edu.njusoftware.dossiermanagement.controller.UserController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class VoiceSpeech {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    // 初始化设置SpeechUtility对象
    static {
        SpeechUtility.createUtility("appid=" + "5e227036");
        SpeechRecognizer.createRecognizer();
    }

    private static List<String> resultList = new LinkedList<>();

    /**
     * 调用讯飞语音识别sdk，识别pcm音频文件
     * @param filePath
     */
    public static List<String> RecognizePcmfileByte(String filePath) {
        resultList.clear();

        SpeechRecognizer recognizer = SpeechRecognizer.getRecognizer();
        recognizer.setParameter(SpeechConstant.AUDIO_SOURCE, "-1");
        // 此参数设置接口返回纯文本，无时间信息
//        speechRecognize.setParameter( SpeechConstant.RESULT_TYPE, "plain" );

        // 设置用户停止说话多长时间后表示结束，最大为10s
        recognizer.setParameter(SpeechConstant.VAD_EOS, "10000");
        recognizer.startListening(recognizerListener);

        FileInputStream fis = null;
        final byte[] buffer = new byte[64*1024];
        try {
            fis = new FileInputStream(new File(filePath));
            if (0 == fis.available()) {
                recognizer.cancel();
            } else {
                int lenRead = buffer.length;
                while( buffer.length==lenRead){
                    lenRead = fis.read( buffer );
                    recognizer.writeAudio( buffer, 0, lenRead );
                }

                recognizer.stopListening();

                // 等待音频解析完成，recognizer.isListening()返回true，说明解析工作还在进行
                while(recognizer.isListening()) {
                    Thread.sleep(500);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != fis) {
                    fis.close();
                    fis = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }//end of try-catch-finally
        return resultList;
    }

    /**
     * 听写监听器
     */
    private static RecognizerListener recognizerListener = new RecognizerListener(){
        public void onBeginOfSpeech(){
            logger.debug("Start to speech");
        }

        public void onEndOfSpeech() {
            logger.debug("..录音结束..");
        }

        /**
         * 获取听写结果. 获取RecognizerResult类型的识别结果，并对结果进行累加
         * @param result
         * @param islast
         */
        public void onResult(RecognizerResult result, boolean islast) {
            resultList.add(result.getResultString());


//            if (islast) {
//                iatSpeechInitUI();
//            }
        }

        public void onVolumeChanged(int volume) {
            logger.debug("onVolumeChanged enter");
        }

        public void onError(SpeechError error) {
            if (null != error) {
                logger.error("onError enter " + error.toString());
            }
        }
        public void onEvent(int eventType,int arg1,int agr2, String msg) {
            logger.debug("onEvent enter");
        }
    };

    /**
     * 听写结束，恢复初始状态
     */
    public void iatSpeechInitUI() {
        //labelWav.setIcon(new ImageIcon("res/mic_01.png"));
        //jbtnRecognizer.setEnabled(true);
        //((JLabel) jbtnRecognizer.getComponent(0)).setText("开始听写");
    }
}
