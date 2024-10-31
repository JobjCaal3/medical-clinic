package voll.med.doctors.domain.doctor.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record DtoUpdateDoctor(@NotNull Long id,
                              String firstName,
                              String lastName,
                              LocalDate birthdate,
                              String phoneNumber) {
}
