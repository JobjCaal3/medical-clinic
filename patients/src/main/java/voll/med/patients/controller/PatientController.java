package voll.med.patients.controller;

import jakarta.validation.Valid;
import org.apache.http.protocol.ResponseServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import voll.med.patients.domain.patient.dto.DtoRegisterPatient;
import voll.med.patients.domain.patient.dto.DtoResponseBriefPatient;
import voll.med.patients.domain.patient.dto.DtoResponsePatient;
import voll.med.patients.domain.patient.dto.DtoUpdatePatient;
import voll.med.patients.domain.patient.service.PatientService;

@RestController
@RequestMapping("/patients/")
public class PatientController {

    private PatientService patientService;
    @Autowired
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping("register-patient")
    public ResponseEntity<DtoResponsePatient> registerPatient(@RequestBody @Valid DtoRegisterPatient dtoRegisterPatient,
                                                              UriComponentsBuilder uriComponentsBuilder){
        return patientService.registerPatient(dtoRegisterPatient, uriComponentsBuilder);
    }

    @PutMapping("update-patient")
    public ResponseEntity<DtoResponsePatient> updatePatient(@RequestBody @Valid DtoUpdatePatient dtoUpdatePatient){
        return patientService.updatePatient(dtoUpdatePatient);
    }

    @DeleteMapping("delete-patient/{id}")
    public ResponseEntity<?> deletePatient(@PathVariable Long id){
        return patientService.deletePatient(id);
    }

    @GetMapping("details-patient/{id}")
    public ResponseEntity<DtoResponsePatient> detailsPatient(@PathVariable Long id){
        return patientService.detailsPatient(id);
    }

    @GetMapping("List-all-patient")
    public ResponseEntity<Page<DtoResponseBriefPatient>> listAllPatient(@PageableDefault(size = 5, sort = {"firstName", "lastName"}) Pageable pageable){
        return patientService.listAllPatient(pageable);
    }

    @GetMapping("list-all-actives-patients")
    public ResponseEntity<Page<DtoResponseBriefPatient>> listAllActivesPatients(@PageableDefault(size = 5, sort = {"firstName", "lastName"}) Pageable pageable){
        return patientService.listAllActivesPatients(pageable);
    }

    @GetMapping("list-all-inactives-patients")
    public ResponseEntity<Page<DtoResponseBriefPatient>> listAllInactivesPatients(@PageableDefault(size = 5, sort = {"firstName", "lastName"}) Pageable pageable){
        return patientService.listAllInactivesPatients(pageable);
    }

    @GetMapping("search-patient/{name}")
    public ResponseEntity<Page<DtoResponseBriefPatient>> serachPatientName(@PathVariable String name,
                                                                           @PageableDefault(size = 5, sort = {"firstName", "lastName"}) Pageable pageable) {
        return patientService.searchPatientName(name, pageable);
    }
}
