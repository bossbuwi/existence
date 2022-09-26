package com.stargazerstudios.existence.conductor.config;

import com.stargazerstudios.existence.conductor.constants.WebSecurityURI;
import com.stargazerstudios.existence.conductor.filters.JwtAuthenticationEntryPoint;
import com.stargazerstudios.existence.conductor.filters.JwtAuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig {

    @Resource(name = "UserServiceImpl")
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    JwtAuthorizationFilter jwtAuthorizationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors();
        http.headers().frameOptions().disable();
        http.csrf().disable()
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers(WebSecurityURI.Unguarded.URI_H2).permitAll()
                .antMatchers(WebSecurityURI.Unguarded.URI_CONCERTO).permitAll()
                .antMatchers(WebSecurityURI.Unguarded.URI_FRONTEND_ROOT).permitAll()
                .antMatchers(WebSecurityURI.Unguarded.URI_FRONTEND_CSS).permitAll()
                .antMatchers(WebSecurityURI.Unguarded.URI_FRONTEND_JS).permitAll()
                .antMatchers(WebSecurityURI.Unguarded.URI_BALLAD_UNGUARDED).permitAll()
                .antMatchers(WebSecurityURI.Unguarded.URI_CONCERTO_UNGUARDED).permitAll()
                .antMatchers(WebSecurityURI.Unguarded.URI_ELIGIUS_UNGUARDED).permitAll()
                .antMatchers(WebSecurityURI.Unguarded.URI_REQUIEM_UNGUARDED).permitAll()
                .antMatchers(WebSecurityURI.Unguarded.URI_SONATA_UNGUARDED).permitAll()
                .antMatchers(WebSecurityURI.Unguarded.URI_SYMPHONY_UNGUARDED).permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager (AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
