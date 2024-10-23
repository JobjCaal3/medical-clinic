package voll.med.doctors.domain.DTO;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import voll.med.doctors.domain.model.doctor.Specialty;

import java.time.LocalDate;

public record DtoRegisterDoctor(@NotBlank(message = "enter your first name") String firstName,
                                @NotBlank(message = "enter tour last name") String lastName,
                                @NotNull(message = "enter your birthdate") LocalDate birthdate,
                                @NotBlank(message = "enter your phoneNumber") String phoneNumber,
                                @NotNull(message = "plesae enter one specialty") Specialty specialty,
                                @NotBlank @Email(message = "email invalid, enter a valid email") String email) {

}
