package med.voll.security_service.domain.client.doctor.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import med.voll.security_service.domain.client.doctor.model.Specialty;
import med.voll.security_service.domain.user.model.User;

import java.time.LocalDate;

public record DtoDoctorRegisterData(@NotBlank String firstName,
                                    @NotBlank String lastName,
                                    @NotNull LocalDate birthdate,
                                    @NotBlank @Size(min = 9, max = 15)
                                    String phoneNumber,
                                    @NotNull Specialty specialty,
                                    @NotBlank @Email String email,
                                    Long userId) {


    public DtoDoctorRegisterData(DtoDoctorRegisterData doctorRegisterData, User user) {
        this(doctorRegisterData.firstName, doctorRegisterData.lastName, doctorRegisterData.birthdate,
                doctorRegisterData.phoneNumber, doctorRegisterData.specialty, doctorRegisterData.email,
                user.getId());
    }
}
