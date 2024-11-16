package med.voll.security_service.domain.client.doctor.feing;

import jakarta.validation.Valid;
import med.voll.security_service.domain.client.doctor.dto.DtoDoctorRegisterData;
import med.voll.security_service.domain.client.doctor.dto.DtoResponseDataDoctor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "doctors")
public interface IDoctorClient {
    @PostMapping("/doctors/register-doctor")
    ResponseEntity<DtoResponseDataDoctor> registerDoctor(@RequestBody @Valid DtoDoctorRegisterData dtoRegisterDoctor);
}
