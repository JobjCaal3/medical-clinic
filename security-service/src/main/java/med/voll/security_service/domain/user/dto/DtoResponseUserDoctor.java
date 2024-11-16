package med.voll.security_service.domain.user.dto;

import med.voll.security_service.domain.client.doctor.dto.DtoDoctorRegisterData;
import med.voll.security_service.domain.client.doctor.dto.DtoResponseDataDoctor;
import med.voll.security_service.domain.user.model.Role;
import med.voll.security_service.domain.user.model.User;
import org.springframework.http.ResponseEntity;

public record DtoResponseUserDoctor(Long id,
                                    String userName,
                                    Role role,
                                    DtoResponseDataDoctor responseDataDoctor) {


    public DtoResponseUserDoctor(User user, DtoResponseDataDoctor doctor) {
        this(user.getId(), user.getUserName(), user.getRole(), new DtoResponseDataDoctor(doctor));
    }
}
