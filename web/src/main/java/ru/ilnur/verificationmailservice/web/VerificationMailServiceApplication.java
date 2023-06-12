package ru.ilnur.verificationmailservice.web;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import ru.ilnur.verificationmailservice.facade.model.AccountFacade;
import ru.ilnur.verificationmailservice.facade.model.ContractorEmailFacade;
import ru.ilnur.verificationmailservice.web.mixins.AccountMixin;
import ru.ilnur.verificationmailservice.web.mixins.ContractorEmailMixin;

@Slf4j
@SpringBootApplication
@PropertySource({"classpath:security.properties","classpath:web.properties"})
@ConfigurationPropertiesScan
public class VerificationMailServiceApplication {

    public static final int MIN_PASSWORD_LENGTH = 6;

    public static void main(String[] args) {
        SpringApplication.run(VerificationMailServiceApplication.class, args);
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return jacksonObjectMapperBuilder -> {
            jacksonObjectMapperBuilder.mixIn(AccountFacade.class, AccountMixin.class);
            jacksonObjectMapperBuilder.mixIn(ContractorEmailFacade.class, ContractorEmailMixin.class);

            jacksonObjectMapperBuilder.featuresToDisable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            jacksonObjectMapperBuilder.featuresToEnable(SerializationFeature.WRITE_DATES_WITH_ZONE_ID);
            jacksonObjectMapperBuilder.defaultViewInclusion(true);
        };
    }
}
