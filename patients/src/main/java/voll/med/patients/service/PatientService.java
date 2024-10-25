package voll.med.patients.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import voll.med.patients.domain.repository.IPatientRepository;
@Service
public class PatientService {
    private IPatientRepository patientRepository;

    @Autowired
    public PatientService(IPatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

}
