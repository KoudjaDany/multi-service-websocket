package com.training.multiservicewebsocket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //.authorizeRequests()
                //.antMatchers("/*")
                //.permitAll()
                //.and()
                .headers().frameOptions().sameOrigin().and()
                .csrf().disable()
                .formLogin()
                    .defaultSuccessUrl("/index.html")
                    .loginPage("/login.html")
                    .failureUrl("/login.html?error")
                    .permitAll()
                    .and()
                .logout()
                    .logoutSuccessUrl("/login.html?logout")
                    .logoutUrl("/logout.html")
                    .permitAll()
                    .and()
                .authorizeRequests()
                    .antMatchers("/static/**").permitAll()
                    .antMatchers("/webjars/**").permitAll()
                    .anyRequest().authenticated()
                    .and();
        ;
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        auth
                .inMemoryAuthentication()
                .withUser("dany").password(encoder.encode("dan")).roles("USER").and()
                .withUser("cedric").password(encoder.encode("cedric")).roles("USER").and()
                .withUser("idris").password(encoder.encode("idris")).roles("USER").and()
                .withUser("ashley").password(encoder.encode("ashley")).roles("USER").and()
                .withUser("koudja").password(encoder.encode("kou")).roles("ADMIN", "USER");
    }
}
