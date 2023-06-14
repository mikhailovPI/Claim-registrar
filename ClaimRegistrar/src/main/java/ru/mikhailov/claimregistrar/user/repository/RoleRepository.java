package ru.mikhailov.claimregistrar.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mikhailov.claimregistrar.user.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String name);
}
