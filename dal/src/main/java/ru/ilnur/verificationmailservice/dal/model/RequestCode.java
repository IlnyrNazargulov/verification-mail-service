package ru.ilnur.verificationmailservice.dal.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Getter
@Entity
@Table(name = "request_code")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RequestCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(updatable = false, nullable = false)
    private String email;
    @Column(updatable = false, nullable = false)
    private String code;
    @Column(updatable = false, nullable = false)
    private Instant createdAt;
    @Setter
    private boolean isUsed = false;

    public RequestCode(String email, String code, Instant createdAt) {
        this.email = email;
        this.code = code;
        this.createdAt = createdAt;
    }
}
