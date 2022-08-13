package com.platform.platform;

import com.platform.platform.User.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableWebMvc
public class PlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlatformApplication.class, args);
	}

}
