package med.voll.security_service.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import med.voll.security_service.domain.client.patient.dto.DtoPatientRegisterData;

public record DtoRegisterPatientUser(@NotBlank String userName,
                                     @NotBlank String password,
                                     DtoPatientRegisterData patientRegisterData) {
}
