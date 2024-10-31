package voll.med.patients.domain.client.doctor.dto;

public record DtoRequestDoctor(Long id,
                               String firstName,
                               String lastName,
                               String phoneNumber,
                               String specialty,
                               String email,
                               Boolean active) {
}
