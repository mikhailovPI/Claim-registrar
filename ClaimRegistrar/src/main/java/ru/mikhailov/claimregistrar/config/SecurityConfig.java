package ru.mikhailov.claimregistrar.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    public void setUserDetailsService(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/request/admin/**").hasRole(String.valueOf(UserRole.ADMIN))
                .antMatchers("/request/operator/**").hasAnyRole(
                        String.valueOf(UserRole.ADMIN),
                        String.valueOf(UserRole.OPERATOR))
                .antMatchers("/request/user/**").hasAuthority(String.valueOf(UserRole.USER))

//                .antMatchers("/request/admin/**").hasRole(String.valueOf(UserRole.ADMIN))
//                .antMatchers("/request/operator/**").hasAnyRole(
//                        String.valueOf(UserRole.ADMIN),
//                        String.valueOf(UserRole.OPERATOR))
//                .antMatchers("/request/user/**").hasAuthority(String.valueOf(UserRole.USER))
                .antMatchers("/registration/**").permitAll()
                .and()
                .formLogin()
                .and()
                .logout().logoutSuccessUrl("/");

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
    protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        authenticationProvider.setUserDetailsService(userDetailsService);
        return authenticationProvider;
    }
}
