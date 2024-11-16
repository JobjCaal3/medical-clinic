package med.voll.security_service.domain.client.patient.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import med.voll.security_service.domain.user.model.User;

import java.time.LocalDate;

public record DtoPatientRegisterData(@NotBlank String firstName,
                                     @NotBlank String lastName,
                                     @NotBlank String gender,
                                     @NotNull LocalDate birthdaydate,
                                     @NotBlank String phoneNumber,
                                     @NotBlank @Email String email,
                                     @NotNull Long userId) {
    public DtoPatientRegisterData(DtoPatientRegisterData patient, User user) {
        this(patient.firstName, patient.lastName, patient.gender, patient.birthdaydate, patient.phoneNumber, patient.email,
                user.getId());
    }
}
