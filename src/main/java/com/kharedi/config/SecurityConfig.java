package com.kharedi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.thymeleaf.extras.springsecurity6.dialect.SpringSecurityDialect;

import com.kharedi.repository.RoleRepository;
import com.kharedi.repository.UserRepository;
import com.kharedi.service.CustomUserDetailsService;
import com.kharedi.service.Implements.UserSecurityService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled=true)
public class SecurityConfig {
    private final Environment env;

    @Autowired
    private UserSecurityService userSecurityService;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    SecurityConfig(Environment env) {
        this.env = env;
    }

    @Bean
    SpringSecurityDialect springSecurityDialect() {
        return new SpringSecurityDialect();
    }

    private static final String[] PUBLIC_MATCHERS = {
            "/css/**",
            "/js/**",
            "/",
            "/images/**",
            "/product/**",
            "/newUser",
            "/forgetPassword",
            "/fonts/**",
            "/register"
    };

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
        .authorizeHttpRequests((authorizeHttpRequests) ->
			authorizeHttpRequests
			.requestMatchers("/admin/**").hasAuthority("ADMIN_ROLE")
	        .requestMatchers(PUBLIC_MATCHERS).permitAll()
	        .anyRequest().authenticated()
			)
            .formLogin(formLogin ->
                formLogin
                    .failureUrl("/login?error")
                    .defaultSuccessUrl("/product")
                    .loginPage("/login")
                    .permitAll()
            )
            .logout(logout ->
                logout
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutSuccessUrl("/?logout")
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
                    .permitAll()
            )
            .oauth2Login(oauth2Login -> {
                oauth2Login
                    .loginPage("/login")
                    .successHandler(new CustomAuthenticationSuccessHandler(userRepository,roleRepository));
            });

        return http.build();
    }

    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService);
    }
}
