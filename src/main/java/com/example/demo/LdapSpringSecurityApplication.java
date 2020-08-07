package com.example.demo;

import java.security.Principal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class LdapSpringSecurityApplication {

	public static void main(String[] args) {
		SpringApplication.run(LdapSpringSecurityApplication.class, args);
	}

}

@RestController
class GreetingsRestController {

	@GetMapping("/greeting")
	String greet(Principal p) {
		return "hello, " + p.getName() + "!";
	}

}

@Configuration
@EnableWebSecurity
class LdapConfiguration extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		auth.ldapAuthentication().userDnPatterns("uid={0},ou=people")
				.groupSearchBase("ou=groups").contextSource()
				.url("ldap://localhost:8389/dc=springframework,dc=org").and()
				.passwordCompare()//.passwordEncoder(new LdapShaPasswordEncoder())
				.passwordAttribute("userPassword");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().and().httpBasic().and().authorizeRequests().anyRequest()
				.authenticated();
	}

}


