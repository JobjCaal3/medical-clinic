package voll.med.doctors.domain.doctor.dto;

import voll.med.doctors.domain.doctor.model.Doctor;
import voll.med.doctors.domain.doctor.model.Specialty;

public record DtoResponseBriefDoctor(Long id,
                                     String firstName,
                                     String lastName,
                                     String phoneNumber,
                                     Specialty specialty,
                                     String email,
                                     Boolean active) {
    public DtoResponseBriefDoctor(Doctor doctor){
        this(doctor.getId(), doctor.getFirstName(), doctor.getLastName(), doctor.getPhoneNumber(), doctor.getSpecialty(),
                doctor.getEmail(), doctor.getActive());
    }
}
