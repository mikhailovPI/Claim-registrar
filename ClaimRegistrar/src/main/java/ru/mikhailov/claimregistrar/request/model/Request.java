package ru.mikhailov.claimregistrar.request.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import ru.mikhailov.claimregistrar.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = Request.TABLE_REQUESTS, schema = Request.SCHEMA_TABLE)
public class Request {

    public static final String TABLE_REQUESTS = "requests";
    public static final String SCHEMA_TABLE = "public";
    public static final String REQUEST_ID = "request_id";
    public static final String REQUEST_TEXT = "text_request";
    public static final String REQUEST_CREATE_ON = "created_on";
    public static final String REQUEST_USER_ID = "user_id";
    public static final String REQUEST_STATUS = "status";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = REQUEST_ID)
    Long id;

    @Column(name = REQUEST_TEXT)
    String text;

    @Column(name = REQUEST_CREATE_ON)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss", iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime publishedOn;

    @OneToOne
    @JoinColumn(name = REQUEST_USER_ID)
    User user;

    @Enumerated(EnumType.STRING)
    @Column(name = REQUEST_STATUS)
    RequestStatus status;


}
