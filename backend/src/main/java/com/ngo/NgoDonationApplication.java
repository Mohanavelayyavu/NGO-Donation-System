package com.ngo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
 * Main Class
 * Starts the Spring Boot Application
 */

@SpringBootApplication
public class NgoDonationApplication {

    public static void main(String[] args) {

        com.ngo.util.DBInitializer.initialize();

        SpringApplication.run(NgoDonationApplication.class, args);

        System.out.println("--------------------------------------");
        System.out.println("NGO Donation Tracking System Started");
        System.out.println("Server Running : http://localhost:8080");
        System.out.println("--------------------------------------");

    }

}