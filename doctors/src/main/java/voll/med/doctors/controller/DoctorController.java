package voll.med.doctors.controller;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import voll.med.doctors.domain.doctor.dto.DtoRegisterDoctor;
import voll.med.doctors.domain.doctor.dto.DtoResponseBriefDoctor;
import voll.med.doctors.domain.doctor.dto.DtoResponseDoctor;
import voll.med.doctors.domain.doctor.dto.DtoUpdateDoctor;
import voll.med.doctors.domain.doctor.service.DoctorService;

@RestController
@RequestMapping("/doctors/")
public class DoctorController {
    private DoctorService doctorService;

    @Autowired
    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @PostMapping("register-doctor")
    @Transactional
    public ResponseEntity<DtoResponseDoctor> registerDoctor(@RequestBody @Valid DtoRegisterDoctor dtoRegisterDoctor, UriComponentsBuilder uriComponentsBuilder) {
        return doctorService.registerDoctor(dtoRegisterDoctor, uriComponentsBuilder);
    }

    @PutMapping("update-doctor")
    @Transactional
    public ResponseEntity<DtoResponseDoctor> updateDoctor(@RequestBody @Valid DtoUpdateDoctor dtoUpdateDoctor) {
        return doctorService.updateDoctor(dtoUpdateDoctor);
    }

    @DeleteMapping("deleted-doctor/{id}")
    @Transactional
    public ResponseEntity deletedDoctor(@PathVariable Long id) {
        return doctorService.deletedDoctor(id);
    }

    @GetMapping("details-doctor/{id}")
    public ResponseEntity<DtoResponseDoctor> detailsDoctor(@PathVariable Long id) {
        return doctorService.detailsDoctor(id);
    }

    @GetMapping("list-all-doctors")
    public ResponseEntity<Page<DtoResponseBriefDoctor>> listAllDoctors(@PageableDefault(size = 5,
            sort = {"firstName", "lastName"}) Pageable pageable) {
        return doctorService.listAllDoctors(pageable);
    }

    @GetMapping("list-all-doctors-actives")
    public ResponseEntity<Page<DtoResponseBriefDoctor>> listAllDoctorsActives(@PageableDefault(size = 5,
            sort = {"firstName", "lastName"}) Pageable pageable) {
        return doctorService.listAllDoctorsActives(pageable);
    }

    @GetMapping("list-all-doctors-inactives")
    public ResponseEntity<Page<DtoResponseBriefDoctor>> listAllDoctorsIncatives(@PageableDefault(size = 5,
            sort = {"firstName", "lastName"}) Pageable pageable) {
        return doctorService.listAllDoctorsIncatives(pageable);
    }

    @GetMapping("search-doctor-speciality/{specialty}")
    public ResponseEntity<Page<DtoResponseBriefDoctor>> serachDoctorSpeciality(@PathVariable String specialty,
                                                                               @PageableDefault(size = 5, sort = {"firstName", "lastName"}) Pageable pageable) {
        return doctorService.searchDoctorSpeciality(specialty, pageable);
    }

    @GetMapping("search-doctor/{name}")
    public ResponseEntity<Page<DtoResponseBriefDoctor>> searchDoctorName(@PathVariable String name,
                                                                         @PageableDefault(size = 5, sort = {"firstName", "lastName"}) Pageable pageable) {
        return doctorService.searchDoctorName(name, pageable);
    }

    @PutMapping("assign-patient")
    public ResponseEntity<?> assignPatientDoctor(@RequestParam Long patientId){
        return doctorService.assingPatientDoctor(patientId);
    }
    //todo make a list doctor for amount patient

    //todo make a list patient for doctor

}
