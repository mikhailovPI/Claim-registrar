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
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = Role.TABLE_ROLES, schema = Role.SCHEMA_TABLE)
public class Role {

    public static final String TABLE_ROLES = "roles";
    public static final String SCHEMA_TABLE = "public";
    public static final String ROLE_ID = "role_id";
    public static final String ROLE_NAME = "role_name";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ROLE_ID)
    Long id;

    @Column(name = ROLE_NAME)
    String name;
}
