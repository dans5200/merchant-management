package com.mpc.merchant;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.jpos.q2.Q2;
import org.jpos.q2.iso.QMUX;
import org.jpos.util.NameRegistrar;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MerchantApplication {
	private static Logger log = LogManager.getLogger(MerchantApplication.class);

	public static void main(String[] args) {
		BasicConfigurator.configure();
		SpringApplication.run(MerchantApplication.class, args);
	}
}
