package ru.mikhailov.claimregistrar.user.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level =  AccessLevel.PRIVATE)
@Entity
@Table(name = User.TABLE_USERS, schema = User.SCHEMA_TABLE)
public class User implements UserDetails {

    public static final String TABLE_USERS = "users";
    public static final String SCHEMA_TABLE = "public";
    public static final String USERS_ID = "user_id";
    public static final String USERS_NAME = "user_name";
    public static final String USERS_EMAIL = "email";
    public static final String USERS_ADMIN = "user_admin";
    public static final String USERS_OPERATOR = "user_operator";
    private static final String USERS_PASSWORD = "user_password";
    private static final String USERS_ROLE = "user_role";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = USERS_ID)
    Long id;

    @Column(name = USERS_NAME)
    String name;

    @Column(name = USERS_PASSWORD)
    String password;

    @Column(name = USERS_EMAIL)
    String email;

    @Enumerated(EnumType.STRING)
    @ElementCollection (targetClass = UserRole.class, fetch = FetchType.EAGER)
    @CollectionTable(name = USERS_ROLE, joinColumns = @JoinColumn(name = USERS_ID))
    private Set<UserRole> roleSet = new HashSet<>();

    @Column(name = USERS_ADMIN)
    Boolean admin = false;

    @Column(name = USERS_OPERATOR)
    Boolean operator = false;


    //TODO: Блок от UserDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roleSet;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
