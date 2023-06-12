package ru.ilnur.verificationmailservice.web.contracts;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.io.PrintWriter;
import java.io.StringWriter;

@Getter
public class ErrorBody {

    private final ErrorCodes errorCode;

    private final String message;

    private final String trace;

    @JsonIgnore
    private final HttpStatus httpStatus;

    public ErrorBody(ErrorCodes errorCode, String message, Exception exception, HttpStatus httpStatus) {
        this.errorCode = errorCode;
        this.message = message;
        this.httpStatus = httpStatus;
        StringWriter stringWriter = new StringWriter();
        exception.printStackTrace(new PrintWriter(stringWriter));
        this.trace = stringWriter.toString();
    }
}
