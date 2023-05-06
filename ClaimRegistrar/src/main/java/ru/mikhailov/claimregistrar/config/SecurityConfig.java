//package ru.mikhailov.claimregistrar.config;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import ru.mikhailov.claimregistrar.request.service.CustomUserDetailsSetvice;
//import ru.mikhailov.claimregistrar.user.model.UserRole;
//
//@EnableWebSecurity
//@RequiredArgsConstructor
//public class SecurityConfig extends WebSecurityConfigurerAdapter {
//
//    private final CustomUserDetailsSetvice customUserDetailsSetvice;
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(customUserDetailsSetvice)
//                .passwordEncoder(encoder());
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//
//        http.authorizeRequests()
//                .antMatchers("/request/admin").hasRole(String.valueOf(UserRole.ADMIN))
//                .antMatchers("/request/operator").hasAnyRole(
//                        String.valueOf(UserRole.OPERATOR),
//                        String.valueOf(UserRole.ADMIN))
//                .antMatchers("/request/user").hasRole(String.valueOf(UserRole.USER))
//                .and()
//                .formLogin();
//    }
//
//    @Bean
//    public PasswordEncoder encoder() {
//        return new BCryptPasswordEncoder();
//    }
//}
