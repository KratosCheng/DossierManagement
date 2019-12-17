package edu.njusoftware.dossiermanagement.service.impl;

import edu.njusoftware.dossiermanagement.domain.Dossier;
import edu.njusoftware.dossiermanagement.domain.DossierOperationRecord;
import edu.njusoftware.dossiermanagement.mapper.DossierMapper;
import edu.njusoftware.dossiermanagement.repository.DossierOperationRepository;
import edu.njusoftware.dossiermanagement.repository.DossierRepository;
import edu.njusoftware.dossiermanagement.service.IDossierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DossierServiceImpl implements IDossierService {

    @Autowired
    private DossierRepository dossierRepository;

    @Autowired
    private DossierOperationRepository dossierOperationRepository;

    @Autowired
    private DossierMapper dossierMapper;

    @Override
    public Dossier getDossier(long id) {
        Optional<Dossier> optionalDossier = dossierRepository.findById(id);
        return optionalDossier.orElse(null);
    }

    @Override
    public List<Dossier> getDossiersByCaseNum(String caseNum) {
        return dossierRepository.findAllByCaseNum(caseNum);
    }

    @Override
    public boolean saveDossier(Dossier dossier) {
        return dossierRepository.save(dossier) != null;
    }

    @Override
    public List<DossierOperationRecord> getDossierOperationRecordsByDossierId(long dossierId) {
        return dossierOperationRepository.findAllByDossierId(dossierId);
    }

    @Override
    public List<DossierOperationRecord> getDossierOperationRecordsByCaseNum(String caseNum) {
        return dossierMapper.findRecordsByCaseNum(caseNum);
    }

    @Override
    public List<DossierOperationRecord> getDossierOperationRecordsByJobNum(String jobNum) {
        return dossierOperationRepository.findAllByJobNum(jobNum);
    }

    private boolean saveOperationRecord(DossierOperationRecord dossierOperationRecord) {
        return dossierOperationRepository.save(dossierOperationRecord) != null;
    }
}
