package med.voll.security_service.domain.client.patient.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record DtoResponseDataPatient(Long Id,
                                     String firstName,
                                     String lastName,
                                     String gender,
                                     LocalDate birthdaydate,
                                     String phoneNumber,
                                     String email,
                                     Boolean active,
                                     LocalDateTime entryDate) {
    public DtoResponseDataPatient(DtoResponseDataPatient patient) {
        this(patient.Id, patient.firstName, patient.lastName, patient.gender, patient.birthdaydate, patient.phoneNumber,
                patient.email, patient.active, patient.entryDate);
    }
}
