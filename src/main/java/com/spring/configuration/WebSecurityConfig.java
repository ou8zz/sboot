package com.spring.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    
	@Autowired
	protected DataSource dataSource;
	
	@Override
    protected void configure(HttpSecurity http) throws Exception {
        // login
		http.authorizeRequests().antMatchers("/", "/h2/**", "/global/**").permitAll().anyRequest().authenticated()
        .and().formLogin().loginPage("/login").loginProcessingUrl("/web_login").successForwardUrl("/index")
        .failureHandler(authenticationFailureHandler())
        .successHandler(authenticationSuccessHandler()).permitAll();
        
        // logout
        http.logout().logoutUrl("/logout").logoutSuccessHandler(new LogoutHandle()).permitAll();
        
        // rememberme
        http.rememberMe().tokenRepository(persistentTokenRepository()).tokenValiditySeconds(1209600);
        
//        http.authorizeRequests().antMatchers("/").permitAll().and().authorizeRequests().antMatchers("/console/**").permitAll();
        http.csrf().disable();
        http.headers().disable();
    }
    
	
	/**
	 * 登录校验处理
	 */
	@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(new UserDetailService());
    }
	
	/**
	 * rememberme 数据库配置
	 * @return
	 */
	@Bean
	public PersistentTokenRepository persistentTokenRepository() {
		JdbcTokenRepositoryImpl db = new JdbcTokenRepositoryImpl();
		db.setDataSource(dataSource);
		return db;
	}
	
	/**
	 * 用户登录失败处理
	 * @return LoginFailureHandler
	 */
	@Bean
	protected LoginFailureHandler authenticationFailureHandler() {
		return new LoginFailureHandler();
	}

	/**
	 * 用户登录成功处理
	 * @return LoginSuccessHandle
	 */
	@Bean
	protected LoginSuccessHandle authenticationSuccessHandler() {
		return new LoginSuccessHandle();
	}

}


