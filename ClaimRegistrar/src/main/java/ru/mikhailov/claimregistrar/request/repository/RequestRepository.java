package ru.mikhailov.claimregistrar.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.mikhailov.claimregistrar.config.PageRequestOverride;
import ru.mikhailov.claimregistrar.request.model.Request;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request> findRequestsByUserId(Long userId, PageRequestOverride pageRequest);

    @Query("SELECT r FROM Request r WHERE LOWER(r.user.name) LIKE CONCAT('%', LOWER(:namePart), '%')")
    List<Request> findOrdersByUserNamePart(@Param("namePart") String namePart, PageRequestOverride pageRequest);

    void deleteRequestsByUserId(Long userId);
}
