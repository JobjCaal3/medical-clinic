package med.voll.security_service.domain.client.patient.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import med.voll.security_service.domain.user.model.User;

import java.time.LocalDate;

public record DtoPatientRegisterData(@NotBlank String firstName,
                                     @NotBlank String lastName,
                                     @NotBlank String gender,
                                     @NotNull LocalDate birthdaydate,
                                     @NotBlank @Size(min = 9, max = 15) String phoneNumber,
                                     @NotBlank @Email String email,
                                     Long userId) {
    public DtoPatientRegisterData(DtoPatientRegisterData patient, User user) {
        this(patient.firstName, patient.lastName, patient.gender, patient.birthdaydate, patient.phoneNumber, patient.email,
                user.getId());
    }
}
