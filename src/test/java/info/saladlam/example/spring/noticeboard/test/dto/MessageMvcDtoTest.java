package info.saladlam.example.spring.noticeboard.test.dto;

import info.saladlam.example.spring.noticeboard.dto.MessageDto;
import info.saladlam.example.spring.noticeboard.dto.MessageMvcDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

import static info.saladlam.example.spring.noticeboard.dto.MessageMvcDto.DATE_TIME_FORMATTER;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
class MessageMvcDtoTest {

    private static final String VALID_DATE = "2020-01-01";
    private static final String VALID_TIME = "12:00";
    private static final String VALID_DATE_2 = "2021-01-01";
    private static final String VALID_TIME_2 = "23:59";
    private static final String INVALID_DATE = "01-01-2020";
    private static final String INVALID_TIME = "99:99";
    private static final String VALID_DESCRIPTON = "Hello world!";
    private static final LocalDateTime VALID_LOCAL_DATE_TIME = LocalDateTime.parse(VALID_DATE + " " + VALID_TIME, DATE_TIME_FORMATTER);
    private static final LocalDateTime VALID_LOCAL_DATE_TIME_2 = LocalDateTime.parse(VALID_DATE_2 + " " + VALID_TIME_2, DATE_TIME_FORMATTER);

    private void testAllEmpty(MessageMvcDto dto) {
        assertThat(dto.getPublishDateDate()).isEmpty();
        assertThat(dto.getPublishDateTime()).isEmpty();
        assertThat(dto.getRemoveDateDate()).isEmpty();
        assertThat(dto.getRemoveDateTime()).isEmpty();
        assertThat(dto.getDescription()).isEmpty();
        assertThat(dto.getPublishDate()).isNull();
        assertThat(dto.getRemoveDate()).isNull();
    }

    private void testDto(MessageMvcDto dto) {
        assertThat(dto.getPublishDateDate()).isEqualTo(VALID_DATE);
        assertThat(dto.getPublishDateTime()).isEqualTo(VALID_TIME);
        assertThat(dto.getRemoveDateDate()).isEqualTo(VALID_DATE_2);
        assertThat(dto.getRemoveDateTime()).isEqualTo(VALID_TIME_2);
        assertThat(dto.getDescription()).isEqualTo(VALID_DESCRIPTON);
        assertThat(dto.getPublishDate()).isEqualTo(VALID_LOCAL_DATE_TIME);
        assertThat(dto.getRemoveDate()).isEqualTo(VALID_LOCAL_DATE_TIME_2);
    }

    @Test
    void constructor() {
        testAllEmpty(new MessageMvcDto());
    }

    @Test
    void constructorEmptyMessageDto() {
        MessageDto messageDto = new MessageDto();
        testAllEmpty(new MessageMvcDto(messageDto));
    }

    @Test
    void constructorMessageDto() {
        MessageDto messageDto = new MessageDto();
        messageDto.setPublishDate(VALID_LOCAL_DATE_TIME);
        messageDto.setRemoveDate(VALID_LOCAL_DATE_TIME_2);
        messageDto.setDescription(VALID_DESCRIPTON);
        testDto(new MessageMvcDto(messageDto));
    }

    @Test
    void fillInformaton() {
        MessageMvcDto dto = new MessageMvcDto();
        dto.setPublishDateDate(VALID_DATE);
        dto.setPublishDateTime(VALID_TIME);
        dto.setRemoveDateDate(VALID_DATE_2);
        dto.setRemoveDateTime(VALID_TIME_2);
        dto.setDescription(VALID_DESCRIPTON);
        testDto(dto);
    }

    @Test
    void allNull() {
        MessageMvcDto dto = new MessageMvcDto();
        dto.setPublishDateDate(null);
        dto.setPublishDateTime(null);
        dto.setRemoveDateDate(null);
        dto.setRemoveDateTime(null);
        dto.setDescription(null);
        testAllEmpty(dto);
    }

    @Test
    void incompleteDateTime() {
        MessageMvcDto dto = new MessageMvcDto();
        dto.setPublishDateTime(VALID_TIME);
        dto.setRemoveDateDate(VALID_DATE);
        assertThat(dto.getPublishDate()).isNull();
        assertThat(dto.getRemoveDate()).isNull();

        dto = new MessageMvcDto();
        dto.setPublishDateDate(VALID_DATE);
        dto.setRemoveDateTime(VALID_TIME);
        assertThat(dto.getPublishDate()).isNull();
        assertThat(dto.getRemoveDate()).isNull();
    }

    @Test
    void invalidDateTime() {
        MessageMvcDto dto = new MessageMvcDto();
        dto.setPublishDateTime(INVALID_TIME);
        dto.setPublishDateDate(INVALID_DATE);
        assertThat(dto.getPublishDate()).isNull();
    }

}
