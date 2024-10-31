package voll.med.patients.domain.patient.dto;

import voll.med.patients.domain.client.doctor.dto.DtoRequestDoctor;

public record DtoResponsePatientByDoctor(DtoResponseBriefPatient responseBriefPatient,
                                         DtoRequestDoctor requestDoctor) {
}
