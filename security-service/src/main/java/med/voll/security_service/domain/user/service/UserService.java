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

    /**
     * is responsible for creation a user with all data's.
     * {@link PasswordEncoder} it is used to encrypt the password.
     * {@code userExist} is responsible for checking if the user exists in the database
     * @param registerDoctorUser contains the user registration data and {@code doctorRegisterData}  this contains
     *                           all data's of the doctor.
     * {@link IDoctorClient} this class contains the endpoint for register doctor.
     * @param uriComponentsBuilder
     * @return the data's of de user and of the data doctor's
     */
    public ResponseEntity<DtoResponseUserDoctor> registerDoctorUser(DtoRegisterDoctorUser registerDoctorUser, UriComponentsBuilder uriComponentsBuilder) {
        Optional<User> userExist = userRepository.findByUserName(registerDoctorUser.userName());
        if (userExist.isPresent()) {
            throw new ValidationIntegration("username in use");
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

    /**
     * is responsible for creation a user with all data's.
     * {@link PasswordEncoder} it is used to encrypt the password.
     * {@code userExist} is responsible for checking if the user exists in the database
     * @param registerPatientUser contains the user registration data and {@code patientRegisterData}  this contains
     *                           all data's of the patient.
     * {@link IPatientClient} this class contains the endpoint for register patient.
     * @param uriComponentsBuilder
     * @return the data's of de user and of the data patient's
     */
    public ResponseEntity<DtoResponseUserPatient> registerPatientUser(DtoRegisterPatientUser registerPatientUser, UriComponentsBuilder uriComponentsBuilder) {
        Optional<User> userExist = userRepository.findByUserName(registerPatientUser.userName());
        if (userExist.isPresent()) {
            throw new ValidationIntegration("username in use");
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

    /**
     * {@link #loginUser(DtoLoginUser)} is responsible for start of season of the user, always and where enter
     * correctly their credentials.
     * in this method we are checking if the username exists in the database or not,
     * the count it's blocked or not if the count is blocking throw an exception to this count is blocked,
     * is this password is correcting through a method belonging to {@link PasswordEncoder} {@code matche}.
     * if it passes all the checks, the token is created
     * @param loginUser contains de data for start of season, such as password and username
     * @return the created token
     */
    public ResponseEntity<DtoResponseLoginUser> loginUser(DtoLoginUser loginUser) {
        User user = userRepository.findByUserName(loginUser.userName()).orElseThrow(() -> new ValidationIntegration("Incorrect password or username, enter your credentials correctly"));

        if (!user.getAccountNonLocked())
            throw new ValidationIntegration("this account is bloked");

        if (!passwordEncoder.matches(loginUser.password(), user.getPassword()))
            throw new ValidationIntegration("Incorrect password or username, enter your credentials correctly");

        String token = tokenService.createToken(user);
        return ResponseEntity.ok(new DtoResponseLoginUser(token));
    }
}
