package com.sitedia.common.rest.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;

import com.sitedia.common.rest.utils.CustomAuthenticationSuccessHandler;
import com.sitedia.common.rest.utils.InitializingBean;
import com.sitedia.common.rest.utils.Sha512PasswordEncoder;

/**
 * Security configuration
 * @author cedric
 *
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
@EnableJdbcHttpSession
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private InitializingBean initializingBean;

    @Autowired
    private DataSource dataSource;

    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(dataSource).usersByUsernameQuery(initializingBean.getUsersByUsernameQuery())
                .authoritiesByUsernameQuery(initializingBean.getAuthoritiesByUsernameQuery())
                .passwordEncoder(new Sha512PasswordEncoder(initializingBean.getSalt()));
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {

        // Accept paths on white list
        http.authorizeRequests().antMatchers(initializingBean.getAllowedPaths().split(",")).permitAll();
        http.authorizeRequests().anyRequest().denyAll();

        // Disable frames
        http.headers().frameOptions().disable();
        http.headers().contentTypeOptions().disable();

        // Authentification
        http.formLogin().loginProcessingUrl("/login.html").usernameParameter("username").passwordParameter("password")
                .successHandler(new CustomAuthenticationSuccessHandler());
        http.logout().logoutUrl("/logout.html").invalidateHttpSession(true).clearAuthentication(true).deleteCookies("SESSION");

        // Disable CSRF
        http.csrf().disable();
    }

}
