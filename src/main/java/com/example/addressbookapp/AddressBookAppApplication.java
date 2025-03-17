package com.example.addressbookapp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;

@Slf4j
@SpringBootApplication
@EnableCaching
public class AddressBookAppApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(AddressBookAppApplication.class, args);
		String activeProfile = context.getEnvironment().getActiveProfiles().length > 0
				? context.getEnvironment().getActiveProfiles()[0]
				: "default";
		log.info("Address Book App Started in {} environment", activeProfile);
	}

}
