package ru.mikhailov.claimregistrar.user.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level =  AccessLevel.PRIVATE)
@Entity
@Table(name = User.TABLE_USERS, schema = User.SCHEMA_TABLE)
public class User {

    public static final String TABLE_USERS = "users";
    public static final String SCHEMA_TABLE = "public";
    public static final String USERS_ID = "user_id";
    public static final String USERS_NAME = "user_name";
    public static final String USERS_EMAIL = "email";
    public static final String USERS_ADMIN = "user_admin";
    public static final String USERS_OPERATOR = "user_operator";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = USERS_ID)
    Long id;

    @Column(name = USERS_NAME)
    String name;

    @Column(name = USERS_EMAIL)
    String email;

    @Column(name = USERS_ADMIN)
    Boolean admin = false;

    @Column(name = USERS_OPERATOR)
    Boolean operator = false;
}
