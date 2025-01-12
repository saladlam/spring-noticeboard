package info.saladlam.example.spring.noticeboard.dto;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.Objects;

public class MessageMvcDtoValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return MessageMvcDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        MessageMvcDto dto = (MessageMvcDto) target;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "publishDateDate", "error.emptyDate");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "publishDateTime", "error.emptyTime");
        if ((!dto.getRemoveDateDate().isBlank() && dto.getRemoveDateTime().isBlank()) ||
                (dto.getRemoveDateDate().isBlank() && !dto.getRemoveDateTime().isBlank())) {
            errors.rejectValue("removeDate", "error.dateTimeMissing", null, null);
        }
        if (Objects.nonNull(dto.getPublishDate()) && Objects.nonNull(dto.getRemoveDate()) && !(dto.getRemoveDate().isAfter(dto.getPublishDate()))) {
            errors.reject("error.removeSooner");
        }
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", "error.emptyDescription");
    }

}
