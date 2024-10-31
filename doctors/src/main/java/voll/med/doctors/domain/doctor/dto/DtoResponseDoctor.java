package voll.med.doctors.domain.doctor.dto;

import voll.med.doctors.domain.doctor.model.Doctor;
import voll.med.doctors.domain.doctor.model.Specialty;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record DtoResponseDoctor(Long id,
                                String firstName,
                                String lastName,
                                LocalDate birthdate,
                                String phoneNumber,
                                Specialty specialty,
                                String email,
                                LocalDateTime entryDate,
                                Boolean active) {
    public DtoResponseDoctor(Doctor doctor) {
        this(doctor.getId(), doctor.getFirstName(), doctor.getLastName(), doctor.getBirthdate(), doctor.getPhoneNumber(),
        doctor.getSpecialty(), doctor.getEmail(), doctor.getEntryDate(), doctor.getActive());
    }
}
