package voll.med.patients.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import voll.med.patients.service.PatientService;

@RestController
public class PatientController {
    private PatientService patientService;
    @Autowired
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }


}
