package com.segdoc.configuration;

import java.io.IOException;

import javax.activation.DataSource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private DataSource dataSource;
	
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	
	private final String USER_QUERY = "select email, password, active from user where email=?";
	private final String ROLES_QUERY = "select u.email, r.role from user u inner join user_role ur on (u.id = ur.user_id) inner join role r on (ur.role_id) where u.email=?";
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth)throws Exception{
		auth.jdbcAuthentication()
		.usersByUsernameQuery(USER_QUERY)
		.authoritiesByUsernameQuery(ROLES_QUERY)
		.dataSource((javax.sql.DataSource) dataSource)
		.passwordEncoder(bCryptPasswordEncoder);
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception{
		http.authorizeRequests()
		.antMatchers("/").permitAll()
		.antMatchers("/login").permitAll()
		.antMatchers("/signup").permitAll()
		.antMatchers("/home/**").hasAuthority("ADMIN").anyRequest()
		.authenticated().and().csrf().disable()
		.formLogin().loginPage("/login").failureUrl("/login?error=true")
		.defaultSuccessUrl("/home/home")
		.usernameParameter("email")
		.passwordParameter("password")
		.and().logout()
		.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		.logoutSuccessUrl("/")
		.and().rememberMe()
		.tokenRepository(persistentTokenRepository())
		.tokenValiditySeconds(60*60)
		.and().exceptionHandling().accessDeniedPage("/access_denied");
		
	}
	 public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
	            Authentication authentication) throws IOException, ServletException {
	        redirectStrategy.sendRedirect(request, response, "/");
	    }
	
	 @Bean
	 public PersistentTokenRepository persistentTokenRepository() {
	  JdbcTokenRepositoryImpl db = new JdbcTokenRepositoryImpl();
	  db.setDataSource((javax.sql.DataSource) dataSource);
	  
	  return db;
	 }
}
