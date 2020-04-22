package edu.njusoftware.dossiermanagement.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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
        //设置请求头
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("multipart/form-data");
        headers.setContentType(type);

        //设置请求体，注意是LinkedMultiValueMap
        FileSystemResource fileSystemResource = new FileSystemResource(filePath);
        MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
        form.add("file", fileSystemResource);
        form.add("remove_lines", 1);
        form.add("verbose", 1);

        //用HttpEntity封装整个请求报文
        HttpEntity<MultiValueMap<String, Object>> files = new HttpEntity<>(form, headers);

        logger.debug("Call the ocr interface:" + ocrUrl + " to recognize file " + filePath);
        // 调用接口即可
        return restTemplate.postForEntity(ocrUrl, files, String.class).getBody();
    }
}
