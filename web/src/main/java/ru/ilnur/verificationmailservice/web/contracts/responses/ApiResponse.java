package ru.ilnur.verificationmailservice.web.contracts.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import ru.ilnur.verificationmailservice.web.helpers.IdentifyTypeHelper;
import ru.ilnur.verificationmailservice.web.contracts.ErrorBody;

import java.util.Collections;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    @JsonProperty("isSuccess")
    private boolean isSuccess;
    private ResponseType type;
    private T data;
    private List<ErrorBody> errors;

    public ApiResponse(T data) {
        this.isSuccess = true;
        this.data = data;
        this.type = data == null ? ResponseType.NONE : (IdentifyTypeHelper.isArray(data) ? ResponseType.ARRAY : ResponseType.OBJECT);
    }

    public ApiResponse(ErrorBody error) {
        this.isSuccess = false;
        this.errors = Collections.singletonList(error);
    }

    public ApiResponse(List<ErrorBody> errors) {
        this.isSuccess = false;
        this.errors = errors;
    }

    public ApiResponse(ErrorBody error, T data) {
        this.isSuccess = false;
        this.errors = Collections.singletonList(error);
        this.data = data;
        this.type = data == null ? ResponseType.NONE : (IdentifyTypeHelper.isArray(data) ? ResponseType.ARRAY : ResponseType.OBJECT);
    }

    public static ApiResponse error(final ErrorBody errorBody) {
        return new ApiResponse(errorBody);
    }

    public static ApiResponse error(final List<ErrorBody> errorBodies) {
        return new ApiResponse(errorBodies);
    }

    public static ApiResponse error(final ErrorBody errorBody, final Object data) {
        return new ApiResponse(errorBody, data);
    }


    public static <T> ResponseEntity<ApiResponse<T>> failure(final T data) {
        ApiResponse<T> apiResponse = new ApiResponse<>(data);
        apiResponse.setSuccess(false);
        return ResponseEntity.ok(apiResponse);
    }

    public static <T> ResponseEntity<ApiResponse<T>> success(final T data) {
        return ResponseEntity.ok(new ApiResponse<>(data));
    }

    public static <T> ResponseEntity<ApiResponse<T>> success() {
        return ResponseEntity.ok(new ApiResponse<T>((T) null));
    }

    public enum ResponseType {
        OBJECT,
        ARRAY,
        VALUE,
        NONE
    }
}
