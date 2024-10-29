package voll.med.doctors.domain.client.feing;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import voll.med.doctors.domain.client.dto.DtoRequestPatient;

import java.util.List;

@FeignClient(name = "patients")
public interface IPatientClient {
    @GetMapping("/patients/list-patients-by-id")
    List<DtoRequestPatient> getPatientsById(@RequestBody List<Long> patientIds);

}
