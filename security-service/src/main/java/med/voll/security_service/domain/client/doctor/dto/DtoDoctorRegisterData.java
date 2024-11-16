package med.voll.security_service.domain.client.doctor.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import med.voll.security_service.domain.client.doctor.model.Specialty;
import med.voll.security_service.domain.user.dto.DtoRegisterDoctorUser;
import med.voll.security_service.domain.user.model.User;

import java.time.LocalDate;

public record DtoDoctorRegisterData(@NotBlank String firstName,
                                    @NotBlank String lastName,
                                    @NotNull LocalDate birthdate,
                                    @NotBlank String phoneNumber,
                                    @NotNull Specialty specialty,
                                    @NotBlank @Email String email,
                                    @NotNull Long userId) {


    public DtoDoctorRegisterData(DtoDoctorRegisterData doctorRegisterData, User user) {
        this(doctorRegisterData.firstName, doctorRegisterData.lastName, doctorRegisterData.birthdate,
                doctorRegisterData.phoneNumber, doctorRegisterData.specialty, doctorRegisterData.email,
                user.getId());
    }
}
