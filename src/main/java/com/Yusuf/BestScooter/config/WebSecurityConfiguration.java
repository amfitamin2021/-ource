package com.Yusuf.BestScooter.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider
                = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(new BCryptPasswordEncoder());
        return  provider;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//                .antMatchers("/")
//                .permitAll()
//                .antMatchers("/home")
//                .hasAuthority("USER")
//                .antMatchers("/admin")
//                .hasAuthority("ADMIN")
//                .anyRequest()
//                .authenticated()
//                .and()
//                .httpBasic();


        http.authorizeRequests()
                .antMatchers(HttpMethod.POST, "/addUser")
                .permitAll()
                .antMatchers(HttpMethod.POST, "/login")
                .permitAll()
                .antMatchers(HttpMethod.POST, "/root/**", "/root")
                .permitAll()
                .antMatchers(HttpMethod.POST, "/orderadd")
                .permitAll()
                .antMatchers("/register", "./img/**", "./css/**", "/index")
                .permitAll()
                .antMatchers("/root" , "/root/**" , "/root**")
                .hasAuthority("ADMIN")
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
                .logout()
                .permitAll()
                .and()
                .httpBasic();
        http.csrf().disable();

    }
}