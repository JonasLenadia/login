package com.segdoc.SegdocManager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

import com.segdoc.controller.UserController;

//@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@SpringBootApplication
@ComponentScan(basePackageClasses = UserController.class)
public class SegdocManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SegdocManagerApplication.class, args);
	}

}
