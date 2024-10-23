package voll.med.doctors.domain.DTO;

import voll.med.doctors.domain.model.doctor.Doctor;
import voll.med.doctors.domain.model.doctor.Specialty;

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
