package ru.ilnur.verificationmailservice.dal.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.ilnur.verificationmailservice.facade.model.ContractorEmailFacade;
import ru.ilnur.verificationmailservice.facade.model.enums.AccountType;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "contractor_email")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ContractorEmail implements ContractorEmailFacade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(updatable = false, nullable = false)
    private String email;
    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    public ContractorEmail(String email, AccountType accountType) {
        this.email = email;
        this.accountType = accountType;
    }
}
