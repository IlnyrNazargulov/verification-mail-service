package ru.ilnur.verificationmailservice.dal;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EntityScan
@PropertySource("classpath:dal.properties")
@EnableJpaRepositories
@EnableTransactionManagement
public class DalModule {
}
