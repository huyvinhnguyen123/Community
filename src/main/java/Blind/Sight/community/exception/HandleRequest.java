package Blind.Sight.community.exception;

import Blind.Sight.community.web.response.common.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.xml.bind.ValidationException;
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
}
