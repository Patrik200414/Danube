package com.danube.danube;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DanubeApplication {

	public static void main(String[] args) {
		SpringApplication.run(DanubeApplication.class, args);
	}

	public static void test(){
		System.out.println("Hello");
	}

}
