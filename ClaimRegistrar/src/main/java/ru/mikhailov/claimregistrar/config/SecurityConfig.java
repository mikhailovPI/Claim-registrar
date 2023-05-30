package ru.mikhailov.claimregistrar.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import ru.mikhailov.claimregistrar.user.model.UserRole;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;

    @Autowired
    public SecurityConfig(@Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/registration/**").permitAll()
//                .antMatchers("/request/admin/**").hasAuthority(Permission.PERMISSION_ADMIN.getPermission())
//                .antMatchers("/request/operator/**").hasAnyAuthority(
//                        Permission.PERMISSION_OPERATOR.getPermission(),
//                        Permission.PERMISSION_ADMIN.getPermission())
//                .antMatchers("/request/user/**").hasAuthority(Permission.PERMISSION_USER.getPermission())

//                .antMatchers("/request/admin/**").hasRole(String.valueOf(UserRole.ADMIN))
//                .antMatchers("/request/operator/**").hasAnyRole(
//                        String.valueOf(UserRole.OPERATOR),
//                        String.valueOf(UserRole.ADMIN))
//                .antMatchers("/request/user/**").hasRole(String.valueOf(UserRole.USER))
                .anyRequest()
                .authenticated()
                .and()
                .formLogin();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

//    @Bean
//    @Override
//    protected UserDetailsService userDetailsService() {
//        return new InMemoryUserDetailsManager(
//                User.builder()
//                        .username("dsds")
//                        .password(passwordEncoder().encode("dsds"))
////                        .roles(UserRole.USER.name())
//                        .authorities(UserRole.USER.grantedAuthoritySet())
//                        .build()
//        );
//    }

    @Bean
    protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    protected DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());
        return daoAuthenticationProvider;
    }
}
