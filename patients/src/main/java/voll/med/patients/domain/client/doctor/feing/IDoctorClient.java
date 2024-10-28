package voll.med.patients.domain.client.doctor.feing;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "doctors")
public interface IDoctorClient {
    @PutMapping("/doctors/assign-patient")
    void assingPatientDoctor(@RequestParam Long patieneId);

}
