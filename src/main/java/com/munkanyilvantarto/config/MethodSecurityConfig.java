package com.munkanyilvantarto.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class MethodSecurityConfig extends WebSecurityConfigurerAdapter{
	
	private UserDetailsService userService;
	
	@Autowired
	public void setUserService(UserDetailsService userService) {
		this.userService = userService;
	}

	@Autowired
	private void configureAuth(AuthenticationManagerBuilder auth) throws Exception{
		
		auth.userDetailsService(userService);
		
	}
	
	protected void configure(HttpSecurity httpSec) throws Exception{
		
		httpSec
			.authorizeRequests()
				.antMatchers("/admin/**")	
					.hasAuthority("ADMIN")
				.antMatchers("/dolgozo/**")
					.hasAuthority("USER")
				.antMatchers("/elfelejtett")
					.permitAll()
				.antMatchers("/jelszo")
					.permitAll()
				.antMatchers("/jelszo/**")
					.permitAll()
				.antMatchers("/visszaigazolas/**")
					.permitAll()
				.antMatchers("/jelszovalt")
					.permitAll()
					.anyRequest().authenticated()
				.and()
					.formLogin()
					.loginPage("/login")
						.permitAll()
					.and()
						.logout()
							.logoutSuccessUrl("/login?logout")
							.deleteCookies("JSESSIONID")
							.permitAll()
				            .and()
				            .rememberMe().key("uniqueAndSecret");
		
	}

}
