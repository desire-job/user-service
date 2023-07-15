package com.gmail.apachdima.desirejob.userservice.config;

import com.gmail.apachdima.desirejob.commonservice.constant.OpenApiAsset;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsServiceImpl;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeRequests()
            .antMatchers(OpenApiAsset.getAssets()).permitAll()
            .antMatchers("/oauth/token", "/api/v1/auth/sign-in", "/api/v1/auth/sign-up").permitAll()
            .anyRequest()
            .authenticated()
            .and()
            .logout()
            .logoutSuccessUrl("/api/v1/logout")
            .deleteCookies("JSESSIONID")
            .invalidateHttpSession(true)
            .clearAuthentication(true)
            .and()
            .formLogin().disable()
            .httpBasic().disable()
            .exceptionHandling().authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
    }

    /*@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            //.sessionManagement()
            //.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            //.sessionFixation(SessionManagementConfigurer.SessionFixationConfigurer::newSession)
            //.maximumSessions(1)
            //.expiredSessionStrategy(event -> event.getResponse().sendError(HttpStatus.UNAUTHORIZED.value()))
            //.maxSessionsPreventsLogin(false)
            //.and().and()
            .authorizeRequests()
            .antMatchers(OpenApiAsset.getAssets()).permitAll()
            .antMatchers("/api/v1/auth/sign-in", "/api/v1/auth/sign-up").permitAll()
            .anyRequest()
            .authenticated()
            .and()
            .formLogin().disable()
            .httpBasic().disable()
            .exceptionHandling().authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));

        return http.build();
    }*/
}
