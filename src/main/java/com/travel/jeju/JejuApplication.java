package com.travel.jeju;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;

@SpringBootApplication
public class JejuApplication {

	public static void main(String[] args) {

		SpringApplication app = new SpringApplication(JejuApplication.class);
		app.addListeners(new ApplicationPidFileWriter());
		app.run(args);
	}

}
