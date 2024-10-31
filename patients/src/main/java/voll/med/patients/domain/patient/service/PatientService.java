package voll.med.patients.domain.patient.service;

import jakarta.validation.Valid;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import voll.med.patients.domain.client.doctor.dto.DtoRequestDoctor;
import voll.med.patients.domain.patient.dto.*;
import voll.med.patients.domain.patient.model.Patient;
import voll.med.patients.domain.patient.repository.IPatientRepository;
import voll.med.patients.domain.client.doctor.feing.IDoctorClient;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatientService {
    private static final Log log = LogFactory.getLog(PatientService.class);
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
        log.info("guardo al paciente y ahora va a pasar a agregar al doctor " + patient.getId());
        doctorClient.assingPatientDoctor(patient.getId());
        log.info("paso la asignacion de doctor");
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

    public ResponseEntity<DtoResponsePatient> searchPatientById(Long id) {
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

    public ResponseEntity<List<DtoResponseBriefPatient>> getPatientById(List<Long> patientIds) {
        List<DtoResponseBriefPatient> patients = patientRepository.findAllById(patientIds).stream().map(DtoResponseBriefPatient::new).collect(Collectors.toList());
        return ResponseEntity.ok(patients);
    }

    public ResponseEntity<DtoResponsePatientByDoctor> findPatientPrimaryDoctor(Long id) {
        DtoResponseBriefPatient patient = patientRepository.findById(id).map(DtoResponseBriefPatient::new).orElseThrow();
        DtoRequestDoctor dtoRequestDoctor =  doctorClient.findDoctorByPatientId(id);

        DtoResponsePatientByDoctor responsePatientByDoctor = new DtoResponsePatientByDoctor(patient, dtoRequestDoctor);
        return ResponseEntity.ok(responsePatientByDoctor);
    }


}
