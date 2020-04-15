package edu.njusoftware.dossiermanagement.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;

@Component
public class PDFRecognizer {
    private static final Logger logger = LoggerFactory.getLogger(PDFRecognizer.class);

    @Value("${ocr.url}")
    private String ocrUrl;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 识别指定图片文件
     * @param filePath
     * @return
     */
    public String recognizePicture(String filePath) {
        File file = new File(filePath);
        // 文件必须封装成FileSystemResource这个类型后端才能收到附件
        FileSystemResource resource = new FileSystemResource(file);
        // 然后所有参数要封装到MultiValueMap里面
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("file", resource);
        param.add("files", new FileSystemResource[]{resource,resource});
        param.add("remove_lines", 1);
        param.add("verbose", 1);
        // 调用接口即可
        return restTemplate.postForEntity("http://xxxService/api/uploadFile/addUploadFileUrl", param, String.class).getBody();
    }
}
