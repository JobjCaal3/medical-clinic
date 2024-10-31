package voll.med.patients.domain.client.doctor.feing;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import voll.med.patients.domain.client.doctor.dto.DtoRequestDoctor;

@FeignClient(name = "doctors")
public interface IDoctorClient {
    @PutMapping("/doctors/assign-patient")
    void assingPatientDoctor(@RequestParam Long patientId);

    @GetMapping("/doctors/find-doctor-by-patient-id")
    DtoRequestDoctor findDoctorByPatientId(@RequestParam Long patientId);
}
