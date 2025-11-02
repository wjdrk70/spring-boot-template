package com.nexsol.cargo.core.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;

@ConfigurationPropertiesScan(basePackages = "com.nexsol.cargo")
@ComponentScan(basePackages = "com.nexsol.cargo")
@SpringBootApplication(scanBasePackages = "com.nexsol.cargo",exclude = { UserDetailsServiceAutoConfiguration.class })
public class CoreApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoreApiApplication.class, args);
	}

}
