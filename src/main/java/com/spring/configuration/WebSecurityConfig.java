package com.spring.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	protected DataSource dataSource;

	@Override
    protected void configure(HttpSecurity http) throws Exception {
        // filter
        // http.authorizeRequests().antMatchers("/login", "/h2/**", "/global/**", "/**/favicon.ico").permitAll().anyRequest().authenticated();

        // 按URL固定限制
//		http.authorizeRequests().antMatchers("/login", "/h2/**", "/global/**", "/**/favicon.ico").permitAll()
//                .antMatchers(HttpMethod.GET, "/app/**").hasAnyAuthority("ROLE_User", "ROLE_Admin", "ROLE_Audit")
//                .antMatchers(HttpMethod.GET, "/audit/**").hasAnyAuthority("ROLE_Admin", "ROLE_Audit")
//                .antMatchers(HttpMethod.GET, "/admin/**").hasAuthority("ROLE_Admin").anyRequest().authenticated();

        // 根据自定义权限动态控制
        http.addFilterBefore(securityFilter(), FilterSecurityInterceptor.class).authorizeRequests();

		// login
        http.formLogin().loginPage("/login").loginProcessingUrl("/web_login")
		.usernameParameter("uname").passwordParameter("upwd")
        .failureHandler(authenticationFailureHandler())
        .successHandler(authenticationSuccessHandler()).permitAll();

        // logout
        http.logout().logoutUrl("/logout").logoutSuccessHandler(logoutHandle()).permitAll();

        // rememberme
        http.rememberMe().rememberMeParameter("rememberme").tokenRepository(persistentTokenRepository()).tokenValiditySeconds(1209600);

		// 关闭csrf
        http.csrf().disable();
        http.headers().disable();
    }

    /**
     * 自定义一个过滤器并赋值自定义的决策器,授权管理器,资源源数据定义.
     * <p>
     * SpringSecurity的过滤器链调用类:public class FilterChainProxy extends GenericFilterBean
     * <p>
     * 由于不能直接对SpringSecurity的过滤器FilterSecurityInterceptor赋值相关参数,
     * 所以只能重新创建一个FilterSecurityInterceptor过滤器赋给自定义参数后,
     * 把自定义的过滤器加入到SpringSecurity的FilterSecurityInterceptor过滤器前,
     * 因为SpringSecurity的FilterSecurityInterceptor具有防重调用功能,
     * 所以执行完自定义的过滤器FilterSecurityInterceptor后就不会在执行自已的过滤器FilterSecurityInterceptor了.
     * @return
     */
    @Bean
    protected FilterSecurityInterceptor securityFilter() {
        FilterSecurityInterceptor filter = new FilterSecurityInterceptor();
        filter.setAccessDecisionManager(webAccessDecisionManager());
        filter.setSecurityMetadataSource(webSecurityMetadataSource());
        return filter;
    }

    @Bean
    protected WebSecurityMetadataSource webSecurityMetadataSource() {
        return new WebSecurityMetadataSource();
    }

    @Bean
    protected WebAccessDecisionManager webAccessDecisionManager() {
        return new WebAccessDecisionManager();
    }

	/**
	 * 登录校验处理
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService());
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
	 * @return JdbcTokenRepositoryImpl
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
}