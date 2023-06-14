package ru.mikhailov.claimregistrar.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mikhailov.claimregistrar.config.PageRequestOverride;
import ru.mikhailov.claimregistrar.request.model.Request;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request> findRequestsByUserId(Long userId, PageRequestOverride pageRequest);
}
