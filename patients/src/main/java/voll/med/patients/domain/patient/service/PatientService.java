package voll.med.patients.domain.patient.service;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import voll.med.patients.domain.patient.dto.DtoRegisterPatient;
import voll.med.patients.domain.patient.dto.DtoResponseBriefPatient;
import voll.med.patients.domain.patient.dto.DtoResponsePatient;
import voll.med.patients.domain.patient.dto.DtoUpdatePatient;
import voll.med.patients.domain.patient.model.Patient;
import voll.med.patients.domain.patient.repository.IPatientRepository;
import voll.med.patients.domain.client.doctor.feing.IDoctorClient;

import java.net.URI;
import java.util.List;

@Service
public class PatientService {
    private IPatientRepository patientRepository;
    private IDoctorClient doctorClient;

    @Autowired
    public PatientService(IDoctorClient doctorClient, IPatientRepository patientRepository) {
        this.doctorClient = doctorClient;
        this.patientRepository = patientRepository;
    }

    public ResponseEntity<DtoResponsePatient> registerPatient(@Valid DtoRegisterPatient dtoRegisterPatient, UriComponentsBuilder uriComponentsBuilder) {
        Patient patient = patientRepository.save(new Patient(dtoRegisterPatient));
        DtoResponsePatient responsePatient = new DtoResponsePatient(patient);
        doctorClient.assingPatientDoctor(patient.getId());

        URI url = uriComponentsBuilder.path("patients/register-patient/{id}").buildAndExpand(patient.getId()).toUri();
        return ResponseEntity.created(url).body(responsePatient);
    }

    public ResponseEntity<DtoResponsePatient> updatePatient(@Valid DtoUpdatePatient dtoUpdatePatient) {
        Patient patient = patientRepository.findById(dtoUpdatePatient.Id()).orElseThrow();
        patient.update(dtoUpdatePatient);
       return ResponseEntity.ok(new DtoResponsePatient(patient));
    }

    public ResponseEntity<?> deletePatient(Long id) {
        Patient patient = patientRepository.findById(id).orElseThrow();
        patient.delete(id);
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<DtoResponsePatient> detailsPatient(Long id) {
        Patient patient = patientRepository.findById(id).orElseThrow();
        return ResponseEntity.ok(new DtoResponsePatient(patient));
    }

    public ResponseEntity<Page<DtoResponseBriefPatient>> listAllPatient(Pageable pageable) {
        Page<DtoResponseBriefPatient> patients = patientRepository.findAll(pageable).map(DtoResponseBriefPatient::new);
        return ResponseEntity.ok(patients);
    }

    public ResponseEntity<Page<DtoResponseBriefPatient>> listAllActivesPatients(Pageable pageable) {
        Page<DtoResponseBriefPatient> patients = patientRepository.findAllByActiveTrue(pageable).map(DtoResponseBriefPatient::new);
        return ResponseEntity.ok(patients);
    }

    public ResponseEntity<Page<DtoResponseBriefPatient>> listAllInactivesPatients(Pageable pageable) {
        Page<DtoResponseBriefPatient> patients = patientRepository.findAllByActiveFalse(pageable).map(DtoResponseBriefPatient::new);
        return ResponseEntity.ok(patients);
    }

    public ResponseEntity<Page<DtoResponseBriefPatient>> searchPatientName(String name, Pageable pageable) {
        Page<DtoResponseBriefPatient> patients = patientRepository.searchPatientByName(name, pageable).map(DtoResponseBriefPatient::new);
        return ResponseEntity.ok(patients);
    }
}
