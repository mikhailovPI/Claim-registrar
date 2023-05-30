package ru.mikhailov.claimregistrar.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.mikhailov.claimregistrar.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select c.name from User c")
    List<String> findByNameOrderByEmail();

    Optional<User> findByEmail(String email);

    //User findByUsername(String name);
}
