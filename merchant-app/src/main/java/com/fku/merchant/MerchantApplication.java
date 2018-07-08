package com.fku.merchant;

import com.fku.merchant.app.core.runner.MerchantRunner;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

import java.util.Arrays;

@Log4j2
@SpringBootApplication
@ComponentScan("com.fku.*")
public class MerchantApplication implements CommandLineRunner {

	private final MerchantRunner merchantRunner;
	private final Environment environment;

	@Autowired
	public MerchantApplication(MerchantRunner merchantRunner,
							   Environment environment) {
		this.merchantRunner = merchantRunner;
		this.environment = environment;
	}

	public static void main(String[] args) {
		SpringApplication.run(MerchantApplication.class, args);
	}

	@Override
	public void run(String... strings) throws Exception {
		log.info("MerchantApplication is starting with active profile: {}", Arrays.toString(environment.getActiveProfiles()));
		merchantRunner.run();
	}
}
