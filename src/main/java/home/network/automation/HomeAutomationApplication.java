package home.network.automation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HomeAutomationApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomeAutomationApplication.class, args);
	}
}
