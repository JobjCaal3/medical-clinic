package voll.med.doctors.domain.client.dto;

public record DtoRequestPatient(Long Id,
                                String firstName,
                                String lastName,
                                String gender,
                                String phoneNumber,
                                String email,
                                Boolean active) {

}
