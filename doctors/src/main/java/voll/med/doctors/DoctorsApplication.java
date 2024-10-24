package voll.med.doctors;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class DoctorsApplication {

	public static void main(String[] args) {
		SpringApplication.run(DoctorsApplication.class, args);
	}

}
