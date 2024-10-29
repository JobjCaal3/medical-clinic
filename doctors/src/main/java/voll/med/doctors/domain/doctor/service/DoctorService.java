package voll.med.doctors.domain.doctor.service;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import voll.med.doctors.domain.client.dto.DtoRequestPatient;
import voll.med.doctors.domain.client.feing.IPatientClient;
import voll.med.doctors.domain.doctor.dto.*;
import voll.med.doctors.domain.doctor.model.Doctor;
import voll.med.doctors.domain.doctor.repository.IDoctorRepository;

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

    public ResponseEntity<DtoResponseDoctor> registerDoctor(@Valid DtoRegisterDoctor dtoRegisterDoctor, UriComponentsBuilder uriComponentsBuilder) {
        Doctor doctor = doctorRepository.save(new Doctor(dtoRegisterDoctor));
        DtoResponseDoctor dtoResponseDoctor = new DtoResponseDoctor(doctor);
        URI url = uriComponentsBuilder.path("/doctors/search-doctor/{id}").buildAndExpand(doctor.getId()).toUri();

        return ResponseEntity.created(url).body(dtoResponseDoctor);
    }

    public ResponseEntity<DtoResponseDoctor> updateDoctor(@Valid DtoUpdateDoctor dtoUpdateDoctor) {
        Doctor doctor = doctorRepository.findById(dtoUpdateDoctor.id()).orElseThrow();
        doctor.update(dtoUpdateDoctor);
        return ResponseEntity.ok(new DtoResponseDoctor(doctor));
    }

    public ResponseEntity deletedDoctor(Long id) {
        Doctor doctor = doctorRepository.findById(id).orElseThrow();
        doctor.deleted(doctor);
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<DtoResponseDoctor> searchDoctorById(Long id) {
        Doctor doctor = doctorRepository.getReferenceById(id);
        DtoResponseDoctor doctorResponse = new DtoResponseDoctor(doctor);
        return ResponseEntity.ok(doctorResponse);
    }

    public ResponseEntity<Page<DtoResponseBriefDoctor>> listAllDoctors(Pageable pageable) {
        Page<DtoResponseBriefDoctor> doctorPage = doctorRepository.findAll(pageable).map(DtoResponseBriefDoctor::new);
        return ResponseEntity.ok(doctorPage);
    }

    public ResponseEntity<Page<DtoResponseBriefDoctor>> listAllDoctorsActives(Pageable pageable) {
        Page <DtoResponseBriefDoctor> doctors = doctorRepository.findAllByActiveTrue(pageable).map(DtoResponseBriefDoctor::new);
        return ResponseEntity.ok(doctors);
    }

    public ResponseEntity<Page<DtoResponseBriefDoctor>> listAllDoctorsIncatives(Pageable pageable) {
        Page<DtoResponseBriefDoctor> doctors = doctorRepository.findAllByActiveFalse(pageable).map(DtoResponseBriefDoctor::new);
        return ResponseEntity.ok(doctors);
    }

    public ResponseEntity<Page<DtoResponseBriefDoctor>> searchDoctorSpeciality(String specialty, Pageable pageable) {
        Page<DtoResponseBriefDoctor> doctors = doctorRepository.searchDoctorsBySpecialty(specialty, pageable).map(DtoResponseBriefDoctor::new);
        return ResponseEntity.ok(doctors);
    }

    public ResponseEntity<Page<DtoResponseBriefDoctor>> searchDoctorName(String name, Pageable pageable) {
        Page<DtoResponseBriefDoctor> doctors = doctorRepository.findByName(name, pageable).map(DtoResponseBriefDoctor::new);
        return ResponseEntity.ok(doctors);
    }

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

    public ResponseEntity<List<DtoRequestPatient>> listPatientByDoctor(Long id) {
        Doctor doctor = doctorRepository.findById(id).orElseThrow();

        List<DtoRequestPatient> patients = patientClient.getPatientsById(doctor.getPatientId());

        return ResponseEntity.ok(patients);
    }
}
