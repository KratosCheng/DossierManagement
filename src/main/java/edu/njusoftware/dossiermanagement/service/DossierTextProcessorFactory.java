package edu.njusoftware.dossiermanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 根据文件类型获取DossierTextProcessor的工厂
 */
@Service
public class DossierTextProcessorFactory {
    @Autowired
    private Map<String, DossierTextProcessor> dossierTextProcessorMap = new HashMap<>(3);

    public DossierTextProcessor getDossierTextProcessor(String type) {
        return dossierTextProcessorMap.get(type);
    }
}
