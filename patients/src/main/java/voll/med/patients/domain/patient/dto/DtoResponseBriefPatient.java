package voll.med.patients.domain.patient.dto;

import voll.med.patients.domain.patient.model.Patient;

import java.util.List;

public record DtoResponseBriefPatient(Long Id,
                                      String firstName,
                                      String lastName,
                                      String gender,
                                      String phoneNumber,
                                      String email,
                                      Boolean active) {
    public DtoResponseBriefPatient(Patient patient){
        this(patient.getId(), patient.getFirstName(), patient.getLastName(), patient.getGender(), patient.getPhoneNumber(),
                patient.getEmail(), patient.getActive());
    }
}
