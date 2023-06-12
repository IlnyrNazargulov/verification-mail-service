package ru.ilnur.verificationmailservice.service;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import ru.ilnur.verificationmailservice.dal.DalModule;

@ComponentScan
@Configuration
@PropertySource(value = "classpath:service.properties", encoding = "UTF8")
@ConfigurationPropertiesScan
@Import({DalModule.class})
public class ServiceModule {
}