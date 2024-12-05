package voll.med.patients.domain.patient.service;

import jakarta.validation.Valid;
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
import voll.med.patients.infra.exception.ValidationIntegration;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatientService {
    private IPatientRepository patientRepository;
    private IDoctorClient doctorClient;

    @Autowired
    public PatientService(IDoctorClient doctorClient, IPatientRepository patientRepository) {
        this.doctorClient = doctorClient;
        this.patientRepository = patientRepository;
    }

    /**
     * Registers a new patient in the database.
     * Saves a new patient to the database and assigns the patient to a doctor using the Doctor microservice.
     * The patient ID is sent to the Doctor microservice.
     *
     * @param dtoRegisterPatient the details of the patient to register
     * @param uriComponentsBuilder used to build the URI for the created resource
     * @return a ResponseEntity containing the registered patient's details and a location header
     */
    public ResponseEntity<DtoResponsePatient> registerPatient(@Valid DtoRegisterPatient dtoRegisterPatient, UriComponentsBuilder uriComponentsBuilder) {
        Patient patient = patientRepository.save(new Patient(dtoRegisterPatient));

        doctorClient.assingPatientDoctor(patient.getId());

        URI url = uriComponentsBuilder.path("patients/register-patient/{id}").buildAndExpand(patient.getId()).toUri();
        return ResponseEntity.created(url).body(new DtoResponsePatient(patient));
    }
    /**
     * Updates an existing patient's details.
     * Retrieves the patient by ID, updates the entity, and saves it back to the database.
     *
     * @param dtoUpdatePatient the updated details of the patient
     * @return a ResponseEntity containing the updated patient details
     * @throws ValidationIntegration if the patient ID is not found
     */
    public ResponseEntity<DtoResponsePatient> updatePatient(@Valid DtoUpdatePatient dtoUpdatePatient) {
        Patient patient = patientRepository.findById(dtoUpdatePatient.Id()).orElseThrow(()->new ValidationIntegration("Patient not found"));
        patient.update(dtoUpdatePatient);
       return ResponseEntity.ok(new DtoResponsePatient(patient));
    }
    /**
     * disables a patient by their ID.
     * Marks the patient as disabled in the database.
     *
     * @param id the ID of the patient to disabled
     * @return a ResponseEntity with no content
     * @throws ValidationIntegration if the patient ID is not found
     */
    public ResponseEntity<?> deletePatient(Long id) {
        Patient patient = patientRepository.findById(id).orElseThrow(()->new ValidationIntegration("Patient not found"));
        patient.delete(id);
        return ResponseEntity.noContent().build();
    }
    /**
     * Retrieves a specific patient by their ID.
     *
     * @param id the ID of the patient to search
     * @return a ResponseEntity containing the patient's details
     * @throws ValidationIntegration if the patient ID is not found
     */
    public ResponseEntity<DtoResponsePatient> searchPatientById(Long id) {
        Patient patient = patientRepository.findById(id).orElseThrow(()->new ValidationIntegration("Patient not found"));
        return ResponseEntity.ok(new DtoResponsePatient(patient));
    }
    /**
     * Retrieves all patients with pagination.
     *
     * @param pageable the pagination details
     * @return a ResponseEntity containing a paginated list of patients
     */
    public ResponseEntity<Page<DtoResponseBriefPatient>> listAllPatient(Pageable pageable) {
        Page<DtoResponseBriefPatient> patients = patientRepository.findAll(pageable).map(DtoResponseBriefPatient::new);
        return ResponseEntity.ok(patients);
    }
    /**
     * Retrieves all active patients with pagination.
     *
     * @param pageable the pagination details
     * @return a ResponseEntity containing a paginated list of active patients
     */
    public ResponseEntity<Page<DtoResponseBriefPatient>> listAllActivesPatients(Pageable pageable) {
        Page<DtoResponseBriefPatient> patients = patientRepository.findAllByActiveTrue(pageable).map(DtoResponseBriefPatient::new);
        return ResponseEntity.ok(patients);
    }
    /**
     * Retrieves all inactive patients with pagination.
     *
     * @param pageable the pagination details
     * @return a ResponseEntity containing a paginated list of inactive patients
     */
    public ResponseEntity<Page<DtoResponseBriefPatient>> listAllInactivesPatients(Pageable pageable) {
        Page<DtoResponseBriefPatient> patients = patientRepository.findAllByActiveFalse(pageable).map(DtoResponseBriefPatient::new);
        return ResponseEntity.ok(patients);
    }
    /**
     * Searches for patients by their name with pagination.
     *
     * @param name the name of the patient to search
     * @param pageable the pagination details
     * @return a ResponseEntity containing a paginated list of patients matching the name
     */
    public ResponseEntity<Page<DtoResponseBriefPatient>> searchPatientName(String name, Pageable pageable) {
        Page<DtoResponseBriefPatient> patients = patientRepository.searchPatientByName(name, pageable).map(DtoResponseBriefPatient::new);
        return ResponseEntity.ok(patients);
    }
    /**
     * Retrieves a list of patients by their IDs.
     * This method serves as an endpoint consumed by the Doctor microservice.
     * It fetches all patients with the specified IDs and returns their brief details.
     *
     * @param patientIds a list of patient IDs
     * @return a ResponseEntity containing a list of brief patient details
     */
    public ResponseEntity<List<DtoResponseBriefPatient>> getPatientById(List<Long> patientIds) {
        List<DtoResponseBriefPatient> patients = patientRepository.findAllById(patientIds).stream().map(DtoResponseBriefPatient::new).collect(Collectors.toList());
        return ResponseEntity.ok(patients);
    }
    /**
     * Finds the primary doctor for a specific patient.
     * This method consumes an endpoint in the Doctor microservice using OpenFeign. It retrieves
     * the doctor assigned to the specified patient and combines their details with the patient's details.
     *
     * @param id the ID of the patient
     * @return a ResponseEntity containing both the patient's details and their primary doctor's details
     */
    public ResponseEntity<DtoResponsePatientByDoctor> findPatientPrimaryDoctor(Long id) {
        DtoResponseBriefPatient patient = patientRepository.findById(id).map(DtoResponseBriefPatient::new).orElseThrow(()->new ValidationIntegration("Patient not found"));
        DtoRequestDoctor dtoRequestDoctor =  doctorClient.findDoctorByPatientId(id);

        DtoResponsePatientByDoctor responsePatientByDoctor = new DtoResponsePatientByDoctor(patient, dtoRequestDoctor);
        return ResponseEntity.ok(responsePatientByDoctor);
    }
}
