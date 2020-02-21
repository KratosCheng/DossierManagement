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
    private Map<String, DossierTextProcessor> dossierTextProcessorMap;

    @Autowired
    public DossierTextProcessorFactory(Map<String, DossierTextProcessor> dossierTextProcessorMap) {
        this.dossierTextProcessorMap = dossierTextProcessorMap;
    }

    public DossierTextProcessor getDossierTextProcessor(String type) {
        return dossierTextProcessorMap.get(type);
    }
}
