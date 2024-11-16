package med.voll.security_service.Controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import med.voll.security_service.domain.user.dto.*;
import med.voll.security_service.domain.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.lang.reflect.Parameter;

@RestController
@RequestMapping("/authentication/")
public class Controller {
    private UserService userService;
    @Autowired
    public Controller(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("register-user-doctor")
    public ResponseEntity<DtoResponseUserDoctor> registerDoctorUser(@RequestBody @Valid DtoRegisterDoctorUser registerDoctorUser, UriComponentsBuilder uriComponentsBuilder){
        return userService.registerDoctorUser(registerDoctorUser, uriComponentsBuilder);
    }

    @PostMapping("register-user-patient")
    public ResponseEntity<DtoResponseUserPatient> registerPatientUser(@RequestBody @Valid DtoRegisterPatientUser registerPatientUser, UriComponentsBuilder uriComponentsBuilder){
        return userService.registerPatientUser(registerPatientUser, uriComponentsBuilder);
    }

    @PostMapping("login-user")
    public ResponseEntity<DtoResponseLoginUser> loginUser(@RequestBody @Valid DtoLoginUser loginUser){
        return userService.loginUser(loginUser);
    }

    @GetMapping("validate-token")
    public ResponseEntity<Boolean> validateToken(@RequestParam String token){
        return userService.validateToken(token);
    }
}
