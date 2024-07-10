package com.backendapp.bankingsystem.configurations;

import com.backendapp.bankingsystem.security.JwtAuthenticationEntryPoint;
import com.backendapp.bankingsystem.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationEntryPoint point;
    @Autowired
    private JwtAuthenticationFilter filter;


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //
//            @Bean
//        public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception {
//            return builder.getAuthenticationManager();
//        }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable())

                .authorizeRequests()
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/api/accounts/createAccount").permitAll()
                .requestMatchers("/api/customers/saveCustomer").permitAll()
                .requestMatchers("/api/users/getCustomerId/{email}").permitAll()
                .requestMatchers("/api/users/**").authenticated()
                .requestMatchers("api/beneficiaries/**").authenticated()
                .requestMatchers("api/transactions/**").authenticated()
                .and().exceptionHandling(ex -> ex.authenticationEntryPoint(point));
        //.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


}