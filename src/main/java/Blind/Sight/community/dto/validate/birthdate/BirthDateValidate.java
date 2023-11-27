package Blind.Sight.community.dto.validate.birthdate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@RequiredArgsConstructor
public class BirthDateValidate implements ConstraintValidator<ValidBirthDate, String> {
    private static final String CONTENT_VALID_MESSAGE = "{ValidBirthDay}";

    @Override
    public boolean isValid(String birthDay, ConstraintValidatorContext context) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        try {
            LocalDate.parse(birthDay, formatter);
        } catch (DateTimeParseException e) {
            addErrorMessage(context);
            return false;
        }
        return true;
    }

    private void addErrorMessage(ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(BirthDateValidate.CONTENT_VALID_MESSAGE)
                .addConstraintViolation();
    }
}
