package com.example.adeventprocessor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for the Ad Event Click Processor.
 * 
 * This is a distributed system POC for processing ad event clicks.
 */
@SpringBootApplication
public class AdEventClickProcessorApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdEventClickProcessorApplication.class, args);
    }
}
