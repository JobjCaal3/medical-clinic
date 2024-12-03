package med.voll.security_service.domain.client.doctor.dto;

import med.voll.security_service.domain.client.doctor.model.Specialty;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record DtoResponseDataDoctor(Long id,
                                    String firstName,
                                    String lastName,
                                    LocalDate birthdate,
                                    String phoneNumber,
                                    Specialty specialty,
                                    String email,
                                    LocalDateTime entryDate,
                                    Boolean active) {

    public DtoResponseDataDoctor(DtoResponseDataDoctor doctor) {
        this(doctor.id, doctor.firstName, doctor.lastName, doctor.birthdate, doctor.phoneNumber, doctor.specialty,
                doctor.email, doctor.entryDate, doctor.active);
    }
}
