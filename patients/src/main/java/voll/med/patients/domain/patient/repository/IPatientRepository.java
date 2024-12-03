package voll.med.patients.domain.patient.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import voll.med.patients.domain.patient.model.Patient;

@Repository
public interface IPatientRepository extends JpaRepository<Patient, Long> {
    Page<Patient> findAllByActiveTrue(Pageable pageable);
    Page<Patient> findAllByActiveFalse(Pageable pageable);
    @Query("SELECT p FROM Patient p WHERE UPPER(CONCAT(p.firstName, ' ', p.lastName)) LIKE UPPER(CONCAT('%',:name,'%')) AND p.active = true ")
    Page<Patient> searchPatientByName(@Param("name") String name, Pageable pageable);
}
