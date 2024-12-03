package voll.med.doctors.domain.doctor.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import voll.med.doctors.domain.doctor.model.Specialty;

import java.time.LocalDate;

public record DtoRegisterDoctor(@NotBlank String firstName,
                                @NotBlank String lastName,
                                @NotNull LocalDate birthdate,
                                @NotBlank @Size(min = 9, max = 15) String phoneNumber,
                                @NotNull Specialty specialty,
                                @NotBlank @Email String email,
                                @NotNull Long userId) {

}
