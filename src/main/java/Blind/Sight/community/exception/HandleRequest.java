package Blind.Sight.community.exception;

import Blind.Sight.community.web.response.common.ResponseDto;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.xml.bind.ValidationException;
import java.net.SocketTimeoutException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
@RestControllerAdvice
@Slf4j
public class HandleRequest extends ResponseEntityExceptionHandler {

    /**
     * handle bad request for error validate input
     *
     * @param fieldErrors   - input list field error
     * @param bindingResult - input binding result
     * @return -  bad request body
     */
    @ExceptionHandler(ValidationException.class)
    public static ResponseEntity<ResponseDto<Object>> validateRequest(HttpStatus httpStatus,
                                                                      Map<String, String> fieldErrors,
                                                                      BindingResult bindingResult) {
        // show fields and message error
        for(FieldError fieldError: bindingResult.getFieldErrors()) {
            String fieldName = fieldError.getField();
            String errorMessage = fieldError.getDefaultMessage();
            fieldErrors.put(fieldName, errorMessage);
        }
        // return dto response status, message and data
        ResponseDto<Object> responseDto = ResponseDto.build().withHttpStatus(httpStatus)
                .withMessage("Bad Request")
                .withData(fieldErrors);
        return ResponseEntity.badRequest().body(responseDto);
    }

// =====================================================================================================================

    @ExceptionHandler(value = {SocketTimeoutException.class})
    public static ResponseEntity<ResponseDto<Object>> handleSocketTimeoutException(SocketTimeoutException ex) {
        String errorMessage = ApiErrorMessages.INVALID_DATA_SUBMITTED;
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        ApiError apiException = buidApiError(ex, errorMessage, httpStatus);
        return buildResponseDto(apiException, ex);
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    public static ResponseEntity<ResponseDto<Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        String errorMessage = ApiErrorMessages.INVALID_INPUT_FORMAT;
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        ApiError apiException = buidApiError(ex, errorMessage, httpStatus);
        return buildResponseDto(apiException, ex);
    }

    @ExceptionHandler(value = {SignatureException.class})
    public static ResponseEntity<ResponseDto<Object>> handleSignatureException(SignatureException ex) {
        String errorMessage = ApiErrorMessages.ACCESS_DENIED;
        HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
        ApiError apiException = buidApiError(ex, errorMessage, httpStatus);
        return buildResponseDto(apiException, ex);
    }

    @ExceptionHandler(value = {BadCredentialsException.class})
    public static ResponseEntity<ResponseDto<Object>> handleBadCredentialException(BadCredentialsException ex) {
        String errorMessage = ApiErrorMessages.AUTHENTICATION_FAILED;
        HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
        ApiError apiException = buidApiError(ex, errorMessage, httpStatus);
        return buildResponseDto(apiException, ex);
    }

    @ExceptionHandler(value = {DataIntegrityViolationException.class})
    public static ResponseEntity<ResponseDto<Object>> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        String errorMessage = ApiErrorMessages.DUPLICATE_RESOURCE;
        HttpStatus httpStatus = HttpStatus.CONFLICT;
        ApiError apiException = buidApiError(ex, errorMessage, httpStatus);
        return buildResponseDto(apiException, ex);
    }

    @ExceptionHandler(NullPointerException.class)
    public static ResponseEntity<ResponseDto<Object>> handleNullPointException(NullPointerException ex) {
        String errorMessage = ApiErrorMessages.MISSING_REQUIRED_FIELDS;
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        ApiError apiException = buidApiError(ex, errorMessage, httpStatus);
        return buildResponseDto(apiException, ex);
    }

    @ExceptionHandler(Exception.class)
    public static ResponseEntity<ResponseDto<Object>> handleException(Exception ex) {
        String errorMessage = ApiErrorMessages.UNEXPECTED_SERVER_ERROR;
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        ApiError apiException = buidApiError(ex, errorMessage, httpStatus);
        return buildResponseDto(apiException, ex);
    }

    private static ApiError buidApiError(Exception ex, String errorMessage, HttpStatus httpStatus) {
        ApiError apiError = new ApiError();
        apiError.setMessage(errorMessage);
        apiError.setThrowable(ex);
        apiError.setHttpStatus(httpStatus);
        apiError.setZonedDateTime(ZonedDateTime.now(ZoneId.of("Z")));
        return apiError;
    }

    private static ResponseEntity<ResponseDto<Object>> buildResponseDto(ApiError apiError, Exception ex) {
        ResponseDto<Object> responseDto = ResponseDto.build()
                .withHttpStatus(apiError.getHttpStatus())
                .withMessage(apiError.getMessage())
                .withDataAndError(new HashMap<>(), ex.getLocalizedMessage());

        return new ResponseEntity<>(responseDto, responseDto.getHttpStatus());
    }
}
