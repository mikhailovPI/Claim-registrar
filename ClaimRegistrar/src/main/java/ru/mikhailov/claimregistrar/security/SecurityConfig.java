package ru.mikhailov.claimregistrar.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import ru.mikhailov.claimregistrar.security.UserDetailsServiceImpl;

import static ru.mikhailov.claimregistrar.request.controller.RequestAdminController.URL_ADMIN;
import static ru.mikhailov.claimregistrar.request.controller.RequestOperatorController.URL_OPERATOR;
import static ru.mikhailov.claimregistrar.request.controller.RequestUserController.URL_USER;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsServiceImpl userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(URL_ADMIN + "/**")
                //.hasAuthority("ADMIN")
                .permitAll()
                .antMatchers(URL_OPERATOR + "/**")
//                .hasAnyAuthority(
//        "OPERATOR", "ADMIN")
                .permitAll()
                .antMatchers(URL_USER + "/**")
                //.hasAuthority("USER")
                .permitAll()
                .antMatchers("/registration/user").permitAll()
                .and()
                .formLogin()
                .loginPage("/login").permitAll()
//                .defaultSuccessUrl("/home") //добавить эндпоинт home и выдать информацию о пользователе или приложении
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "POST"))
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
//                .logoutSuccessUrl("/login?succLogout=true")
//                .permitAll();
                .logoutSuccessUrl("/");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        authenticationProvider.setUserDetailsService(userDetailsService);
        return authenticationProvider;
    }
}
