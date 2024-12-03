package med.voll.security_service.domain.client.patient.feing;

import jakarta.validation.Valid;
import med.voll.security_service.domain.client.patient.dto.DtoPatientRegisterData;
import med.voll.security_service.domain.client.patient.dto.DtoResponseDataPatient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "patients")
public interface IPatientClient {
    @PostMapping("/patients/register-patient")
    ResponseEntity<DtoResponseDataPatient> registerPatient(@RequestBody @Valid DtoPatientRegisterData dtoPatientRegisterData);
}
