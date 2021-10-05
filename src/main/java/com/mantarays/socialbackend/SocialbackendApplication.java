package com.mantarays.socialbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@EnableAutoConfiguration
public class SocialbackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(SocialbackendApplication.class, args);

		/* System.out.print("[");
		for (int i = 0; i < 100; i++) {
			System.out.print("{");
			System.out.print("\"username\":\"user" + i + "\",");
			System.out.print("\"password\":\"pass" + i + "\",");
			System.out.print("\"email\":\"email" + i + "\"");
			if(i==99) {
				System.out.print("}");
			} else {
				System.out.print("},");
			}
		}
		System.out.print("]"); */
	}
}
