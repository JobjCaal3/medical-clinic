package voll.med.doctors.domain.doctor.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import voll.med.doctors.domain.doctor.model.Doctor;

@Repository
public interface IDoctorRepository extends JpaRepository<Doctor, Long> {
    Page<Doctor> findAllByActiveTrue(Pageable pageable);
    Page<Doctor> findAllByActiveFalse(Pageable pageable);
    @Query("SELECT d FROM Doctor d WHERE UPPER(d.specialty) LIKE CONCAT('%', UPPER(:specialty),'%')")
    Page<Doctor> searchDoctorsBySpecialty(@Param("specialty") String specialty, Pageable pageable);
    @Query("SELECT d FROM Doctor d WHERE UPPER(CONCAT(d.firstName, ' ', d.lastName)) = UPPER(:name) AND d.active = true")
    Page<Doctor> findByName(@Param("name") String name, Pageable pageable);
    Doctor findDoctorByPatientId(Long PatientId);
}
