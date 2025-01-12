package info.saladlam.example.spring.noticeboard.test.dto;

import info.saladlam.example.spring.noticeboard.dto.MessageMvcDto;
import info.saladlam.example.spring.noticeboard.dto.MessageMvcDtoValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
class MessageMvcDtoValidatorTest {

    public static class CustomDto {
    }

    // no state
    private final Validator validator = new MessageMvcDtoValidator();

    @MockitoBean
    private Errors errors;

    private MessageMvcDto buildValidDto() {
        MessageMvcDto dto = new MessageMvcDto();
        dto.setPublishDateDate("2020-01-01");
        dto.setPublishDateTime("12:00");
        dto.setDescription("Hello world!");
        return dto;
    }

    private void configureErrors(MessageMvcDto dto) {
        Mockito.when(errors.getFieldValue("publishDateDate")).thenReturn(dto.getPublishDateDate());
        Mockito.when(errors.getFieldValue("publishDateTime")).thenReturn(dto.getPublishDateTime());
        Mockito.when(errors.getFieldValue("removeDateDate")).thenReturn(dto.getRemoveDateDate());
        Mockito.when(errors.getFieldValue("removeDateTime")).thenReturn(dto.getRemoveDateTime());
        Mockito.when(errors.getFieldValue("description")).thenReturn(dto.getDescription());
    }

    @Test
    void testSupports() {
        assertThat(validator.supports(MessageMvcDto.class)).isTrue();
    }

    @Test
    void testNotSupports() {
        assertThat(validator.supports(CustomDto.class)).isFalse();
    }

    @Test
    void pass() {
        MessageMvcDto dto = buildValidDto();
        configureErrors(dto);
        validator.validate(dto, errors);
        Mockito.verify(errors, Mockito.never()).rejectValue(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any());
        Mockito.verify(errors, Mockito.never()).reject(ArgumentMatchers.any());
    }

    @Test
    void passWithRemoveDate() {
        MessageMvcDto dto = buildValidDto();
        dto.setRemoveDateDate(dto.getPublishDateDate());
        dto.setRemoveDateTime("12:01");
        configureErrors(dto);
        validator.validate(dto, errors);
        Mockito.verify(errors, Mockito.never()).rejectValue(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any());
        Mockito.verify(errors, Mockito.never()).reject(ArgumentMatchers.any());
    }

    @Test
    void allEmpty() {
        InOrder inOrder = Mockito.inOrder(errors);
        MessageMvcDto dto = new MessageMvcDto();
        configureErrors(dto);
        validator.validate(dto, errors);
        inOrder.verify(errors).rejectValue("publishDateDate", "error.emptyDate", null, null);
        inOrder.verify(errors).rejectValue("publishDateTime", "error.emptyTime", null, null);
        inOrder.verify(errors).rejectValue("description", "error.emptyDescription", null, null);
    }

    @Test
    void incompleteRemoveDate1() {
        InOrder inOrder = Mockito.inOrder(errors);
        MessageMvcDto dto = buildValidDto();
        dto.setRemoveDateDate(dto.getPublishDateDate());
        configureErrors(dto);
        validator.validate(dto, errors);
        inOrder.verify(errors).rejectValue("removeDate", "error.dateTimeMissing", null, null);
    }

    @Test
    void incompleteRemoveDate2() {
        InOrder inOrder = Mockito.inOrder(errors);
        MessageMvcDto dto = buildValidDto();
        dto.setRemoveDateTime(dto.getPublishDateTime());
        configureErrors(dto);
        validator.validate(dto, errors);
        inOrder.verify(errors).rejectValue("removeDate", "error.dateTimeMissing", null, null);
    }

    @Test
    void removeSooner1() {
        InOrder inOrder = Mockito.inOrder(errors);
        MessageMvcDto dto = buildValidDto();
        dto.setRemoveDateDate(dto.getPublishDateDate());
        dto.setRemoveDateTime("12:00");
        configureErrors(dto);
        validator.validate(dto, errors);
        inOrder.verify(errors).reject("error.removeSooner");
    }

    @Test
    void removeSooner2() {
        InOrder inOrder = Mockito.inOrder(errors);
        MessageMvcDto dto = buildValidDto();
        dto.setRemoveDateDate(dto.getPublishDateDate());
        dto.setRemoveDateTime("11:59");
        configureErrors(dto);
        validator.validate(dto, errors);
        inOrder.verify(errors).reject("error.removeSooner");
    }

}
