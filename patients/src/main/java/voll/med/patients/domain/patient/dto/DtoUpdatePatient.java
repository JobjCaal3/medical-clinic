package voll.med.patients.domain.patient.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record DtoUpdatePatient(@NotNull Long Id,
                               String firstName,
                               String lastName,
                               LocalDate birthdaydate,
                               String phoneNumber) {
}
