package com.shivam.taskmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Task Manager API - Spring Boot 3 Application Entry Point.
 *
 * A production-grade REST API demonstrating:
 * - JWT authentication with Spring Security
 * - Spring Data JPA with H2/PostgreSQL
 * - Swagger/OpenAPI documentation
 * - Comprehensive testing (JUnit 5, Mockito, MockMvc)
 * - Docker containerization
 * - GitHub Actions CI/CD
 */
@SpringBootApplication
public class TaskManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskManagerApplication.class, args);
    }
}