	// Package structure:
// src/main/java/kr/ac/kopo

// 1. Main Application
package kr.ac.kopo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebProjectApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebProjectApplication.class, args);
    }

}