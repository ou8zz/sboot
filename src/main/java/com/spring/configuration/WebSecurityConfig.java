package com.spring.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
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
        // filter
		http.authorizeRequests().antMatchers("/login", "/h2/**", "/global/**", "/**/favicon.ico").permitAll()
		.antMatchers("/app/**").hasAnyAuthority("ROLE_User","ROLE_Admin","ROLE_Audit")
		.antMatchers("/audit/**").hasAnyAuthority("ROLE_Admin","ROLE_Audit")
		.antMatchers("/admin/**").hasAuthority("ROLE_Admin").anyRequest().authenticated();
		
		// login
        http.formLogin().loginPage("/login").loginProcessingUrl("/web_login")
        .failureHandler(authenticationFailureHandler())
        .successHandler(authenticationSuccessHandler()).permitAll();
        
        // logout
        http.logout().logoutUrl("/logout").logoutSuccessHandler(logoutHandle()).permitAll();
        
        // rememberme
        http.rememberMe().tokenRepository(persistentTokenRepository()).tokenValiditySeconds(1209600);
        
        // 登录校验处理
        http.userDetailsService(userDetailsService());
        
        // 关闭csrf
        http.csrf().disable();
        http.headers().disable();
    }
    
	/**
	 * 登录校验处理
	 * 这里需要使用bean的方式拿才能在userDetailService中拿到sqlSession
	 * 通过configure方式设置auth.userDetailService拿不到sqlSession
	 */
	@Bean
	protected UserDetailService userDetailsService() {
		return new UserDetailService();
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
	 * 声明为Bean因为处理中需要用到sqlSession
	 * @return LoginFailureHandler
	 */
	@Bean
	protected LoginFailureHandler authenticationFailureHandler() {
		return new LoginFailureHandler();
	}

	/**
	 * 用户登录成功处理
	 * 声明为Bean因为处理中需要用到sqlSession
	 * @return LoginSuccessHandle
	 */
	@Bean
	protected LoginSuccessHandle authenticationSuccessHandler() {
		return new LoginSuccessHandle();
	}

	/**
	 * 用户注销成功处理
	 * 声明为Bean因为处理中需要用到sqlSession
	 * @return LogoutHandle
	 */
	@Bean
	protected LogoutHandle logoutHandle() {
		return new LogoutHandle();
	}

	@Bean
	protected EmbeddedServletContainerCustomizer containerCustomizer() {
	    return new EmbeddedServletContainerCustomizer(){
	        @Override
	         public void customize(ConfigurableEmbeddedServletContainer container) {
	        	ErrorPage e400 = new ErrorPage(HttpStatus.BAD_REQUEST, "/error/400");
	    		ErrorPage e403 = new ErrorPage(HttpStatus.FORBIDDEN, "/error/403");
	    		ErrorPage e404 = new ErrorPage(HttpStatus.NOT_FOUND, "/error/404");
	    		ErrorPage e500 = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error/500");
	    		container.addErrorPages(e400, e403, e404, e500);
	        }
	    };
	}

}
