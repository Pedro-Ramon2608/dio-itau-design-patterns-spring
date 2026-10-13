package com.dio.dio_itau_design_patterns_spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Projeto Spring criado via Spring Initializr.
 * O projeto possui os seguintes módulos via Spring Initializr:
 * - Spring Web
 * - Spring Data JPA
 * - H2 Database
 * - OpenFeign
 * Além de conter o OpenAPI para realização de testes da API.
 *
 * @author Pedro-Ramon2608
 */
@EnableFeignClients
@SpringBootApplication
public class DioItauDesignPatternsSpringApplication {

	public static void main(String[] args) {
		SpringApplication.run(DioItauDesignPatternsSpringApplication.class, args);
	}

}
