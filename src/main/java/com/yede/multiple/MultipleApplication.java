package com.yede.multiple;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class MultipleApplication {

	public static void main(String[] args) {
		SpringApplication.run(MultipleApplication.class, args);
	}
}
