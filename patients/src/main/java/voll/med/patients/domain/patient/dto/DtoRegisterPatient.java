package voll.med.patients.domain.patient.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record DtoRegisterPatient(@NotBlank(message = "enter your first name") String firstName,
                                 @NotBlank(message = "enter your last name") String lastName,
                                 @NotBlank(message = "enter your gender") String gender,
                                 @NotNull(message = "enter your birthday date") LocalDate birthdaydate,
                                 @NotBlank(message = "enter your phone number") String phoneNumber,
                                 @NotBlank @Email(message = "please enter your email") String email) {
}
