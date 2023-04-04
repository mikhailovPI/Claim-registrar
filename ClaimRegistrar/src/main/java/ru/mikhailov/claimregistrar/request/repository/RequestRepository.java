package ru.mikhailov.claimregistrar.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mikhailov.claimregistrar.request.model.Request;

public interface RequestRepository extends JpaRepository<Request, Long> {
}
