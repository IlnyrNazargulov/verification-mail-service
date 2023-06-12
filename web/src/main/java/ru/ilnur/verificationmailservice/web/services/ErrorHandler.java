package ru.ilnur.verificationmailservice.web.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.ilnur.verificationmailservice.facade.exceptions.BaseException;
import ru.ilnur.verificationmailservice.web.contracts.ErrorBody;
import ru.ilnur.verificationmailservice.web.contracts.ErrorCodes;
import ru.ilnur.verificationmailservice.web.contracts.responses.ApiResponse;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class ErrorHandler extends ResponseEntityExceptionHandler {
    private final ErrorRegister errorRegister;

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        StringBuilder message = new StringBuilder();
        boolean isFirst = true;
        for (Object error : ex.getBindingResult().getAllErrors()) {
            if (!isFirst) {
                message.append("\n");
            }
            isFirst = false;
            if (error instanceof FieldError) {
                FieldError fieldError = (FieldError) error;
                message.append("Field '")
                        .append(fieldError.getField())
                        .append("' has wrong value '")
                        .append(fieldError.getRejectedValue())
                        .append("' because ")
                        .append(fieldError.getDefaultMessage());
            }
            else {
                log.warn("Unsupported MethodArgumentNotValid inner error class: {}.", error.getClass());
                message.append("Unsupported error");
            }
        }
        ErrorBody errorBody = new ErrorBody(ErrorCodes.NOT_VALID_ARGUMENT, message.toString(), ex, HttpStatus.BAD_REQUEST);
        return handleExceptionInternal(ex, errorBody, headers, status, request);
    }


    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        if (log.isDebugEnabled()) {
            log.warn("Handled exception occurs", ex);
        }
        else {
            // to avoid fill our log with traces
            log.warn("Handled exception occurs, {}: {}", ex.getClass(), ex.getMessage());
        }
        if (body == null) {
            ErrorBody errorBody = createErrorBody(ex);
            status = errorBody.getHttpStatus();
            body = errorBody;
        }
        ApiResponse apiResponse = ApiResponse.error((ErrorBody) body);
        return super.handleExceptionInternal(ex, apiResponse, headers, status, request);
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<Object> handleBaseException(BaseException ex, WebRequest request) {
        final ErrorBody errorBody = createErrorBody(ex);
        return handleExceptionInternal(ex, errorBody, null, errorBody.getHttpStatus(), request);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, null, null, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    private ErrorBody createErrorBody(Exception ex) {
        ErrorBody errorBody = errorRegister.lookup(ex);
        if (errorBody == null) {
            log.error("There is no error body for exception.", ex);
            errorBody = new ErrorBody(ErrorCodes.UNKNOWN_ERROR, "Unhandled exception occurs.", ex, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return errorBody;
    }
}
