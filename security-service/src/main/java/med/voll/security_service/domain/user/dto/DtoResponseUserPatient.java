package med.voll.security_service.domain.user.dto;

import med.voll.security_service.domain.client.doctor.dto.DtoResponseDataDoctor;
import med.voll.security_service.domain.client.patient.dto.DtoResponseDataPatient;
import med.voll.security_service.domain.user.model.Role;
import med.voll.security_service.domain.user.model.User;

public record DtoResponseUserPatient(Long id,
                                     String userName,
                                     Role role,
                                     DtoResponseDataPatient responseDataPatient) {

    public DtoResponseUserPatient(User user, DtoResponseDataPatient patient) {
        this(user.getId(), user.getUserName(), user.getRole(), new DtoResponseDataPatient(patient));
    }
}
