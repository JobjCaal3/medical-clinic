package voll.med.patients;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class PatientsApplication {

	public static void main(String[] args) {
		SpringApplication.run(PatientsApplication.class, args);
	}

}
