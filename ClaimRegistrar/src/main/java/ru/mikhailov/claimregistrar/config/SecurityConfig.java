package ru.mikhailov.claimregistrar.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.mikhailov.claimregistrar.user.model.UserRole;
import ru.mikhailov.claimregistrar.user.security.UserDetailsServiceImpl;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsServiceImpl userDetailsService;

//    @Autowired
//    public void setUserDetailsService(UserDetailsServiceImpl userDetailsService) {
//        this.userDetailsService = userDetailsService;
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/request/admin/**").hasAuthority(String.valueOf(UserRole.ADMIN))
                .antMatchers("/request/operator/**").hasAnyAuthority(
                        String.valueOf(UserRole.ADMIN),
                        String.valueOf(UserRole.OPERATOR))
                .antMatchers("/request/user/**").hasAuthority(String.valueOf(UserRole.USER))

//                .antMatchers("/request/admin/**").hasRole(String.valueOf(UserRole.ADMIN))
//                .antMatchers("/request/operator/**").hasAnyRole(
//                        String.valueOf(UserRole.ADMIN),
//                        String.valueOf(UserRole.OPERATOR))
//                .antMatchers("/request/user/**").hasAuthority(String.valueOf(UserRole.USER))
                .antMatchers("/login").permitAll()
                .antMatchers("/registration/**").permitAll()
                .and()
                .formLogin()
                .and()
                .logout()
                .logoutSuccessUrl("/").permitAll();

//                .authorizeRequests()
//                .antMatchers("/registration/**").permitAll()
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
//                .anyRequest()
//                .authenticated()
//                .and()
//                .formLogin();
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
