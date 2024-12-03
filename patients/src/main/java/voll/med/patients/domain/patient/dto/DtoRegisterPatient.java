package voll.med.patients.domain.patient.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record DtoRegisterPatient(@NotBlank String firstName,
                                 @NotBlank String lastName,
                                 @NotBlank String gender,
                                 @NotNull LocalDate birthdaydate,
                                 @NotBlank String phoneNumber,
                                 @NotBlank @Email String email,
                                 @NotNull Long userId) {
}
