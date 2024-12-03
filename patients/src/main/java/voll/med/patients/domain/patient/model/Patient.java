package voll.med.patients.domain.patient.model;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import voll.med.patients.domain.patient.dto.DtoRegisterPatient;
import voll.med.patients.domain.patient.dto.DtoUpdatePatient;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Table(name = "patient")
@Entity
public class Patient {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "gender")
    private String gender;
    private LocalDate birthdaydate;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "email", unique = true)
    private String email;
    private Boolean active;
    @Column(name = "entry_date")
    private LocalDateTime entryDate;
    @Column(name = "user_id")
    private Long userId;

    public Patient() {}

    public Patient(DtoRegisterPatient dtoRegisterPatient) {
        this.firstName = dtoRegisterPatient.firstName();
        this.lastName = dtoRegisterPatient.lastName();
        this.gender = dtoRegisterPatient.gender();
        this.birthdaydate = dtoRegisterPatient.birthdaydate();
        this.phoneNumber = dtoRegisterPatient.phoneNumber();
        this.email = dtoRegisterPatient.email();
        this.userId = dtoRegisterPatient.userId();
    }

    public void update(DtoUpdatePatient dtoUpdatePatient) {
        if(dtoUpdatePatient.firstName() != null && dtoUpdatePatient.firstName().isBlank()){
            this.firstName = dtoUpdatePatient.firstName();
        }
        if(dtoUpdatePatient.lastName() != null && dtoUpdatePatient.firstName().isBlank()){
            this.lastName = dtoUpdatePatient.lastName();
        }
        if(dtoUpdatePatient.birthdaydate() != null){
            this.birthdaydate = dtoUpdatePatient.birthdaydate();
        }
        if(dtoUpdatePatient.phoneNumber() != null && dtoUpdatePatient.firstName().isBlank()){
            this.phoneNumber = dtoUpdatePatient.phoneNumber();
        }
    }

    public void delete(Long id) {
        this.active = false;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public LocalDate getBirthdaydate() {
        return birthdaydate;
    }

    public void setBirthdaydate(LocalDate birthdaydate) {
        this.birthdaydate = birthdaydate;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Patient patient)) return false;
        return Objects.equals(Id, patient.Id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(Id);
    }
}
