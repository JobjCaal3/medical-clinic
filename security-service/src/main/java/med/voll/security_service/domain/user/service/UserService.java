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
import med.voll.security_service.infra.error.ValidationIntegration;
import med.voll.security_service.infra.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@Service
public class UserService {
    private IUserRepository userRepository;
    private IPatientClient patientClient;
    private IDoctorClient doctorClient;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private TokenService tokenService;
    @Autowired
    public UserService(IUserRepository userRepository, IPatientClient patientClient, IDoctorClient doctorClient, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, TokenService tokenService) {
        this.userRepository = userRepository;
        this.patientClient = patientClient;
        this.doctorClient = doctorClient;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    public ResponseEntity<DtoResponseUserDoctor> registerDoctorUser(DtoRegisterDoctorUser registerDoctorUser, UriComponentsBuilder uriComponentsBuilder) {
        User user = userRepository.findByUserName(registerDoctorUser.userName()).orElseThrow();

        user.setRole(Role.DOCTOR);
        user.setUserName(registerDoctorUser.userName());
        user.setPassword(passwordEncoder.encode(registerDoctorUser.password()));
        user.setAccountNonLocked(true);
        userRepository.save(user);

        DtoDoctorRegisterData doctorRegisterData = new DtoDoctorRegisterData(registerDoctorUser.dtoDoctorRegisterData(), user);
        ResponseEntity<DtoResponseDataDoctor> doctor = doctorClient.registerDoctor(doctorRegisterData);

        URI url = uriComponentsBuilder.path("/authentication/search-user/{id}").buildAndExpand(user.getId()).toUri();
        return ResponseEntity.created(url).body(new DtoResponseUserDoctor(user, doctor.getBody()));
    }


    public ResponseEntity<DtoResponseUserPatient> registerPatientUser(DtoRegisterPatientUser registerPatientUser, UriComponentsBuilder uriComponentsBuilder) {
        User user = userRepository.findByUserName(registerPatientUser.userName()).orElseThrow();

        user.setRole(Role.PATIENT);
        user.setUserName(registerPatientUser.userName());
        user.setPassword(passwordEncoder.encode(registerPatientUser.password()));
        user.setAccountNonLocked(true);
        userRepository.save(user);

        DtoPatientRegisterData patientRegisterData = new DtoPatientRegisterData(registerPatientUser.patientRegisterData(), user);
        ResponseEntity<DtoResponseDataPatient> patient = patientClient.registerPatient(patientRegisterData);

        URI url = uriComponentsBuilder.path("/authentication/search-user/{id}").buildAndExpand(user.getId()).toUri();

        return ResponseEntity.created(url).body(new DtoResponseUserPatient(user, patient.getBody()));
    }

    public ResponseEntity<DtoResponseLoginUser> loginUser(DtoLoginUser loginUser) {
        User user = userRepository.findByUserName(loginUser.userName()).orElseThrow();

        if (!user.getAccountNonLocked()) {
            throw new LockedException("this account is bloked");
        }
        if (!passwordEncoder.matches(loginUser.password(), user.getPassword()))
            return ResponseEntity.badRequest().build();

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginUser.userName(), loginUser.password()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = tokenService.createToken(authentication);
        return ResponseEntity.ok(new DtoResponseLoginUser(token));
    }

    public ResponseEntity<Boolean> validateToken(String token) {
        if (token.isEmpty() || token == null)
            return ResponseEntity.badRequest().body(false);

        String userName = tokenService.extractUsername(token);
        if(!userRepository.findByUserName(userName).isPresent())
            return ResponseEntity.badRequest().body(false);

        boolean isValid = tokenService.validateToken(token);
        if (!isValid) {
            return ResponseEntity.badRequest().body(false);
        }

        return ResponseEntity.ok(true);
    }
}
