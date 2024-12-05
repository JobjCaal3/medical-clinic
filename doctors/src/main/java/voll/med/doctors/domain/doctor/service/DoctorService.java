package voll.med.doctors.domain.doctor.service;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import voll.med.doctors.domain.client.dto.DtoRequestPatient;
import voll.med.doctors.domain.client.feing.IPatientClient;
import voll.med.doctors.domain.doctor.dto.*;
import voll.med.doctors.domain.doctor.model.Doctor;
import voll.med.doctors.domain.doctor.repository.IDoctorRepository;
import voll.med.doctors.infra.exceptions.ValidationIntegration;

import java.net.URI;
import java.util.Comparator;
import java.util.List;

@Service
public class DoctorService {
    private IDoctorRepository doctorRepository;
    private IPatientClient patientClient;
    @Autowired
    public DoctorService(IDoctorRepository doctorRepository, IPatientClient patientClient) {
        this.doctorRepository = doctorRepository;
        this.patientClient = patientClient;
    }

    /**
     * Registers a new doctor in the database.
     *
     * @param dtoRegisterDoctor the DTO containing the doctor's registration details
     * @param uriComponentsBuilder a utility to build the URI for the created resource
     * @return a ResponseEntity containing the created doctor's details with a Location header
     */
    public ResponseEntity<DtoResponseDoctor> registerDoctor(@Valid DtoRegisterDoctor dtoRegisterDoctor, UriComponentsBuilder uriComponentsBuilder) {
        Doctor doctor = doctorRepository.save(new Doctor(dtoRegisterDoctor));

        URI url = uriComponentsBuilder.path("/doctors/search-doctor/{id}").buildAndExpand(doctor.getId()).toUri();

        return ResponseEntity.created(url).body(new DtoResponseDoctor(doctor));
    }

    /**
     * Updates the information of an existing doctor.
     *
     * @param dtoUpdateDoctor the DTO containing the doctor's updated details
     * @return a ResponseEntity containing the updated doctor's details
     */
    public ResponseEntity<DtoResponseDoctor> updateDoctor(@Valid DtoUpdateDoctor dtoUpdateDoctor) {
        Doctor doctor = doctorRepository.findById(dtoUpdateDoctor.id()).orElseThrow(()->new ValidationIntegration("doctor not found"));
        doctor.update(dtoUpdateDoctor);
        return ResponseEntity.ok(new DtoResponseDoctor(doctor));
    }

    /**
     * Marks a doctor as disabled in the database.
     *
     * @param id the ID of the doctor to delete
     * @return a ResponseEntity with no content (204 No Content)
     */
    public ResponseEntity deletedDoctor(Long id) {
        Doctor doctor = doctorRepository.findById(id).orElseThrow(()->new ValidationIntegration("doctor not found"));
        doctor.deleted(doctor);
        return ResponseEntity.noContent().build();
    }

    /**
     * Retrieves a doctor's details by ID.
     *
     * @param id the ID of the doctor to retrieve
     * @return a ResponseEntity containing the doctor's details
     */
    public ResponseEntity<DtoResponseDoctor> searchDoctorById(Long id) {
        Doctor doctor = doctorRepository.findById(id).orElseThrow(()->new ValidationIntegration("doctor not found"));
        DtoResponseDoctor doctorResponse = new DtoResponseDoctor(doctor);
        return ResponseEntity.ok(doctorResponse);
    }

    /**
     * Retrieves a paginated list of all doctors.
     *
     * @param pageable pagination and sorting information
     * @return a ResponseEntity containing a paginated list of doctors
     */
    public ResponseEntity<Page<DtoResponseBriefDoctor>> listAllDoctors(Pageable pageable) {
        Page<DtoResponseBriefDoctor> doctorPage = doctorRepository.findAll(pageable).map(DtoResponseBriefDoctor::new);
        return ResponseEntity.ok(doctorPage);
    }

    /**
     * Retrieves a paginated list of active doctors.
     *
     * @param pageable pagination and sorting information
     * @return a ResponseEntity containing a paginated list of active doctors
     */
    public ResponseEntity<Page<DtoResponseBriefDoctor>> listAllDoctorsActives(Pageable pageable) {
        Page <DtoResponseBriefDoctor> doctors = doctorRepository.findAllByActiveTrue(pageable).map(DtoResponseBriefDoctor::new);
        return ResponseEntity.ok(doctors);
    }

    /**
     * Retrieves a paginated list of inactive doctors.
     *
     * @param pageable pagination and sorting information
     * @return a ResponseEntity containing a paginated list of inactive doctors
     */
    public ResponseEntity<Page<DtoResponseBriefDoctor>> listAllDoctorsIncatives(Pageable pageable) {
        Page<DtoResponseBriefDoctor> doctors = doctorRepository.findAllByActiveFalse(pageable).map(DtoResponseBriefDoctor::new);
        return ResponseEntity.ok(doctors);
    }

    /**
     * Searches for doctors by their specialty.
     *
     * @param specialty the specialty to search for
     * @param pageable pagination and sorting information
     * @return a ResponseEntity containing a paginated list of doctors with the specified specialty
     */
    public ResponseEntity<Page<DtoResponseBriefDoctor>> searchDoctorSpeciality(String specialty, Pageable pageable) {
        Page<DtoResponseBriefDoctor> doctors = doctorRepository.searchDoctorsBySpecialty(specialty, pageable).map(DtoResponseBriefDoctor::new);
        return ResponseEntity.ok(doctors);
    }

    /**
     * Searches for doctors by their name (first or last).
     *
     * @param name the name to search for
     * @param pageable pagination and sorting information
     * @return a ResponseEntity containing a paginated list of doctors matching the specified name
     */
    public ResponseEntity<Page<DtoResponseBriefDoctor>> searchDoctorName(String name, Pageable pageable) {
        Page<DtoResponseBriefDoctor> doctors = doctorRepository.findByName(name, pageable).map(DtoResponseBriefDoctor::new);
        return ResponseEntity.ok(doctors);
    }

    /**
     * Assigns a patient to the doctor with the least number of patients, specializing in "GENERAL_DOCTOR".
     *
     * @param patientId the ID of the patient to assign
     * @return a ResponseEntity indicating the operation's success
     */
    public ResponseEntity<?> assingPatientDoctor(Long patientId) {
        Doctor doctors = doctorRepository.searchDoctorsBySpecialty("GENERAL_DOCTOR", Pageable.unpaged())
                .getContent()
                .stream()
                .min(Comparator.comparing(doctor -> doctor.getPatientId().size()))
                .orElseThrow();

        doctors.getPatientId().add(patientId);
        doctorRepository.save(doctors);
        return ResponseEntity.ok().build();
    }

    /**
     * Retrieves a list of patients assigned to a specific doctor.
     * This method to consume an endpoint in the Patient microservice.
     * It retrieves a list of patient details based on the IDs stored in the doctor entity.
     * The doctor entity contains a list of patient IDs, and these IDs are used to query
     * the Patient microservice for the complete patient information.
     *
     * @param id the ID of the doctor whose patients are being retrieved
     * @return a ResponseEntity containing a list of patients assigned to the doctor
     * @throws ValidationIntegration if the doctor is not found in the database
     */
    public ResponseEntity<List<DtoRequestPatient>> listPatientByDoctor(Long id) {
        Doctor doctor = doctorRepository.findById(id).orElseThrow(()->new ValidationIntegration("doctor not found"));

        List<DtoRequestPatient> patients = patientClient.getPatientsById(doctor.getPatientId());

        return ResponseEntity.ok(patients);
    }

    /**
     * Finds the doctor assigned to a specific patient by their ID.
     * This method acts as an endpoint that the Patient microservice consumes to
     * retrieve information about a patient's primary doctor. The patient ID is used
     * to locate the corresponding doctor from the doctor repository, and a brief
     * response is returned containing the doctor's details.
     *
     * @param patientId the ID of the patient whose primary doctor is being retrieved
     * @return a ResponseEntity containing the doctor's brief details
     */
    public ResponseEntity<DtoResponseBriefDoctor> findDoctorByPatientId(Long patientId) {
        Doctor doctor = doctorRepository.findDoctorByPatientId(patientId);
        DtoResponseBriefDoctor responseBriefDoctor = new DtoResponseBriefDoctor(doctor);
        return ResponseEntity.ok(responseBriefDoctor);
    }
}
