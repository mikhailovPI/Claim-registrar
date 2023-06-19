package ru.mikhailov.claimregistrar.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import ru.mikhailov.claimregistrar.user.model.UserRole;

import static ru.mikhailov.claimregistrar.request.controller.RequestAdminController.URL_ADMIN;
import static ru.mikhailov.claimregistrar.request.controller.RequestExecutorController.URL_EXECUTOR;
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
                .csrf()
                    .disable()
                .authorizeRequests()
                    .antMatchers(URL_ADMIN + "/**")
                        .hasAuthority(String.valueOf(UserRole.ADMIN))
                    .antMatchers(URL_OPERATOR + "/**")
                        .hasAnyAuthority(String.valueOf(UserRole.OPERATOR)
                                , String.valueOf(UserRole.ADMIN))
                    .antMatchers(URL_USER + "/**")
                        .hasAuthority(String.valueOf(UserRole.USER))
                    .antMatchers(URL_EXECUTOR + "/**")
                        .hasAuthority(String.valueOf(UserRole.EXECUTOR))
                    .antMatchers("/registration/user").permitAll()
                .and()
                    .formLogin()
                        .loginPage("/auth/login").permitAll()
                .and()
                    .logout()
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "POST"))
                        .deleteCookies("JSESSIONID")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true);
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
