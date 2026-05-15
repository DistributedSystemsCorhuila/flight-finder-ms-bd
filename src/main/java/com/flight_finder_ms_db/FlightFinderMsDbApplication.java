package com.flight_finder_ms_db;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class FlightFinderMsDbApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlightFinderMsDbApplication.class, args);
	}

}
