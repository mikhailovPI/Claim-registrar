package ru.mikhailov.claimregistrar.user.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = User.TABLE_USERS, schema = User.SCHEMA_TABLE)
public class User {

    public static final String TABLE_USERS = "users";
    public static final String TABLE_USERS_ROLES = "users_roles";
    public static final String SCHEMA_TABLE = "public";
    public static final String USERS_ID = "user_id";
    public static final String USERS_NAME = "user_name";
    private static final String USERS_PASSWORD = "user_password";
    public static final String USERS_EMAIL = "email";
    public static final String USERS_ADMIN = "user_admin";
    public static final String USERS_OPERATOR = "user_operator";
    private static final String ROLE_ID = "role_id";

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

    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(name = TABLE_USERS_ROLES,
            joinColumns = @JoinColumn(name = USERS_ID),
            inverseJoinColumns = @JoinColumn(name = ROLE_ID))
    private Set<Role> userRole = new HashSet<>();
}
