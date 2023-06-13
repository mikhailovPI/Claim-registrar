package ru.mikhailov.claimregistrar.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.mikhailov.claimregistrar.user.model.Role;

import java.util.List;
import java.util.Set;

public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query("select r.id, r.name from Role r")
    Set<Role> findRoleByName(String name);

//    @Query("select r.id, r.name from Role r")
    Role findByName(String name);
}
