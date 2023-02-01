package com.csye6225HW1.config;


import com.csye6225HW1.service.IUsersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfig {

    @Autowired
    IUsersService usersService;

    @Autowired
    BCryptPasswordEncoder PwdEncoder;
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/v1/users/","/v1/users","/healthz","/healthz/");
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        return new AuthenticationProvider(){

            @Override
            //authentication param carry the information(username and password) passed from postman basic auth.
            public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                String username = authentication.getName();
                //getCredentials provide password, after authentication success,destroy it.
                String password = authentication.getCredentials().toString();

                // this step for validate username, if not match, throw UsernameNotFoundException
                UserDetails user = usersService.loadUserByUsername(username);

                //
                if(PwdEncoder.matches(password, user.getPassword())){
                    log.info("Access successful");
                    return new UsernamePasswordAuthenticationToken(username, password, user.getAuthorities());
                }else{
                    log.error("Access denied, Password does not match");
                    throw new BadCredentialsException("Password does not match");
                }

            }

            @Override
            public boolean supports(Class<?> authentication) {
                return authentication.equals(UsernamePasswordAuthenticationToken.class);
            }
        };
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.csrf().disable().authorizeHttpRequests((auth)->{
            auth.anyRequest().authenticated();
        }).httpBasic(withDefaults());
        return http.build();
    }
}

