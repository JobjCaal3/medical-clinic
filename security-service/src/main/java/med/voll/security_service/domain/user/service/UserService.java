package med.voll.security_service.domain.user.service;

import med.voll.security_service.domain.client.doctor.dto.DtoDoctorRegisterData;
import med.voll.security_service.domain.client.doctor.dto.DtoResponseDataDoctor;
import med.voll.security_service.domain.client.doctor.feing.IDoctorClient;
import med.voll.security_service.domain.client.patient.dto.DtoPatientRegisterData;
import med.voll.security_service.domain.client.patient.dto.DtoResponseDataPatient;
import med.voll.security_service.domain.client.patient.feing.IPatientClient;
import med.voll.security_service.domain.user.dto.*;
import med.voll.security_service.domain.user.model.Role;
import med.voll.security_service.domain.user.model.User;
import med.voll.security_service.domain.user.repository.IUserRepository;
import med.voll.security_service.infra.exceptions.ValidationIntegration;
import med.voll.security_service.infra.security.TokenService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@Service
public class UserService {
    private static final Log log = LogFactory.getLog(UserService.class);
    private IUserRepository userRepository;
    private IPatientClient patientClient;
    private IDoctorClient doctorClient;
    private PasswordEncoder passwordEncoder;
    private TokenService tokenService;
    @Autowired
    public UserService(IUserRepository userRepository, IPatientClient patientClient, IDoctorClient doctorClient, PasswordEncoder passwordEncoder, TokenService tokenService) {
        this.userRepository = userRepository;
        this.patientClient = patientClient;
        this.doctorClient = doctorClient;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    public ResponseEntity<DtoResponseUserDoctor> registerDoctorUser(DtoRegisterDoctorUser registerDoctorUser, UriComponentsBuilder uriComponentsBuilder) {
        Optional<User> user = userRepository.findByUserName(registerDoctorUser.userName());
        if (user.isPresent()) {
            throw new ValidationIntegration("user in use");
        }

        User newUser = new User();
        newUser.setRole(Role.DOCTOR);
        newUser.setUserName(registerDoctorUser.userName());
        newUser.setPassword(passwordEncoder.encode(registerDoctorUser.password()));
        newUser.setAccountNonLocked(true);
        userRepository.save(newUser);

        DtoDoctorRegisterData doctorRegisterData = new DtoDoctorRegisterData(registerDoctorUser.dtoDoctorRegisterData(), newUser);
        ResponseEntity<DtoResponseDataDoctor> doctor = doctorClient.registerDoctor(doctorRegisterData);

        URI url = uriComponentsBuilder.path("/authentication/search-user/{id}").buildAndExpand(newUser.getId()).toUri();
        return ResponseEntity.created(url).body(new DtoResponseUserDoctor(newUser, doctor.getBody()));
    }


    public ResponseEntity<DtoResponseUserPatient> registerPatientUser(DtoRegisterPatientUser registerPatientUser, UriComponentsBuilder uriComponentsBuilder) {
        Optional<User> userExist = userRepository.findByUserName(registerPatientUser.userName());
        if (userExist.isPresent()) {
            throw new ValidationIntegration("user in use");
        }

        User newUser = new User();
        newUser.setRole(Role.PATIENT);
        newUser.setUserName(registerPatientUser.userName());
        newUser.setPassword(passwordEncoder.encode(registerPatientUser.password()));
        newUser.setAccountNonLocked(true);
        userRepository.save(newUser);

        DtoPatientRegisterData patientRegisterData = new DtoPatientRegisterData(registerPatientUser.patientRegisterData(), newUser);
        ResponseEntity<DtoResponseDataPatient> patient = patientClient.registerPatient(patientRegisterData);

        URI url = uriComponentsBuilder.path("/authentication/search-user/{id}").buildAndExpand(newUser.getId()).toUri();

        return ResponseEntity.created(url).body(new DtoResponseUserPatient(newUser, patient.getBody()));
    }


    public ResponseEntity<DtoResponseLoginUser> loginUser(DtoLoginUser loginUser) {
        User user = userRepository.findByUserName(loginUser.userName()).orElseThrow();

        if (!user.getAccountNonLocked()) {
            throw new ValidationIntegration("this account is bloked");
        }
        if (!passwordEncoder.matches(loginUser.password(), user.getPassword()))
            return ResponseEntity.badRequest().build();

        String token = tokenService.createToken(user);
        return ResponseEntity.ok(new DtoResponseLoginUser(token));
    }

//    public ResponseEntity<Boolean> validateToken(String token) {
//        if (token.isEmpty() || token == null)
//            return ResponseEntity.badRequest().body(false);
//
//        String userName = tokenService.extractUsername(token);
//        if(!userRepository.findByUserName(userName).isPresent())
//            return ResponseEntity.badRequest().body(false);
//
//        boolean isValid = tokenService.validateToken(token);
//        if (!isValid) {
//            return ResponseEntity.badRequest().body(false);
//        }
//
//        return ResponseEntity.ok(true);
//    }
//    private Collection<GrantedAuthority> getToAuthorities(User user) {
//        return Collections.singleton(new SimpleGrantedAuthority(user.getRole().toString()));
//    }
}
