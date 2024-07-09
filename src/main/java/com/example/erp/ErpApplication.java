package com.example.erp;

import java.time.LocalDate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ErpApplication {

	public static void main(String[] args) {
		SpringApplication.run(ErpApplication.class, args);

		String currentDate = String.valueOf(LocalDate.now());
		System.out.println(currentDate);

	}
}
