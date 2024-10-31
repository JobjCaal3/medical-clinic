package voll.med.patients.domain.patient.dto;

import voll.med.patients.domain.patient.model.Patient;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record DtoResponsePatient(Long Id,
                                 String firstName,
                                 String lastName,
                                 String gender,
                                 LocalDate birthdaydate,
                                 String phoneNumber,
                                 String email,
                                 Boolean active,
                                 LocalDateTime entryDate) {
    public DtoResponsePatient(Patient patient) {
        this(patient.getId(), patient.getFirstName(), patient.getLastName(), patient.getGender(), patient.getBirthdaydate(),
                patient.getPhoneNumber(), patient.getEmail(), patient.getActive(), patient.getEntryDate());
    }
}
