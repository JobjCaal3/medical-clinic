package voll.med.doctors.domain.doctor.model;

import jakarta.persistence.*;
import voll.med.doctors.domain.doctor.dto.DtoRegisterDoctor;
import voll.med.doctors.domain.doctor.dto.DtoUpdateDoctor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Table(name = "doctors")
@Entity(name = "Doctor")
public class Doctor {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    private LocalDate birthdate;
    @Column(name = "phone_number", length = 9)
    private String phoneNumber;
    @Enumerated(EnumType.STRING)
    private Specialty specialty;
    @Column(name = "email", unique = true)
    private String email;
    private Boolean active;
    @Column(name = "entry_date")
    private LocalDateTime entryDate;
    @ElementCollection(fetch = FetchType.LAZY)
    @Column(name = "patient_id")
    private List<Long> patientId = new ArrayList<>();

    public Doctor() {}

    public Doctor(DtoRegisterDoctor dtoRegisterDoctor) {
        this.birthdate = dtoRegisterDoctor.birthdate();
        this.email = dtoRegisterDoctor.email();
        this.firstName = dtoRegisterDoctor.firstName();
        this.lastName = dtoRegisterDoctor.lastName();
        this.phoneNumber = dtoRegisterDoctor.phoneNumber();
        this.specialty = dtoRegisterDoctor.specialty();
        this.active = true;
        this.entryDate = LocalDateTime.now();
    }

    public void update(DtoUpdateDoctor dtoUpdateDoctor) {
        if (dtoUpdateDoctor.firstName() != null){
            this.firstName = dtoUpdateDoctor.firstName();
        }
        if (dtoUpdateDoctor.lastName() != null){
            this.lastName = dtoUpdateDoctor.lastName();
        }
        if (dtoUpdateDoctor.birthdate() != null){
            this.birthdate = dtoUpdateDoctor.birthdate();
        }
        if (dtoUpdateDoctor.phoneNumber() != null){
            this.phoneNumber = dtoUpdateDoctor.phoneNumber();
        }
    }

    public void deleted(Doctor doctor) {
        this.active = false;
    }



    public List<Long> getPatientId() {
        return patientId;
    }

    public void setPatientId(List<Long> patientId) {
        this.patientId = patientId;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(LocalDateTime entryDate) {
        this.entryDate = entryDate;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Specialty getSpecialty() {
        return specialty;
    }

    public void setSpecialty(Specialty specialty) {
        this.specialty = specialty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Doctor doctor)) return false;
        return Objects.equals(id, doctor.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
