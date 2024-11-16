package med.voll.security_service.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import med.voll.security_service.domain.client.doctor.dto.DtoDoctorRegisterData;
import med.voll.security_service.domain.user.model.Role;

public record DtoRegisterDoctorUser(@NotBlank String userName,
                                    @NotBlank String password,
                                    DtoDoctorRegisterData dtoDoctorRegisterData) {
}
