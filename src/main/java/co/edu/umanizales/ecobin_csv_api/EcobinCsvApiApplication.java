package co.edu.umanizales.ecobin_csv_api;



import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import co.edu.umanizales.ecobin_csv_api.model.Location;

@SpringBootApplication
public class EcobinCsvApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcobinCsvApiApplication.class, args);

		Location loc = new Location(1, 2, "Calle 1");
		
		
	}

}
