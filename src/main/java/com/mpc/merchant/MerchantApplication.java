package com.mpc.merchant;

import org.apache.log4j.BasicConfigurator;
import org.jpos.q2.Q2;
import org.jpos.q2.iso.QMUX;
import org.jpos.util.NameRegistrar;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MerchantApplication {

	public static void main(String[] args) {
		Q2 q2 = new Q2();
		q2.start();
		BasicConfigurator.configure();
		SpringApplication.run(MerchantApplication.class, args);
	}

}
