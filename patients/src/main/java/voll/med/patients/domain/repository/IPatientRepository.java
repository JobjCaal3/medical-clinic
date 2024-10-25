package voll.med.patients.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import voll.med.patients.domain.model.patients.Patient;

@Repository
public interface IPatientRepository extends JpaRepository<Long, Patient> {
}
