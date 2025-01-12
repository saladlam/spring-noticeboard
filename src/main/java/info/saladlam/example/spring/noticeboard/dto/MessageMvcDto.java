package info.saladlam.example.spring.noticeboard.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

public class MessageMvcDto {

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    private String publishDateDate = "";
    private String publishDateTime = "";
    private String removeDateDate = "";
    private String removeDateTime = "";
    private String description = "";

    public MessageMvcDto() {
    }

    public MessageMvcDto(MessageDto message) {
        if (Objects.nonNull(message.getPublishDate())) {
            this.publishDateDate = message.getPublishDate().format(DATE_FORMATTER);
            this.publishDateTime = message.getPublishDate().format(TIME_FORMATTER);
        }
        if (Objects.nonNull(message.getRemoveDate())) {
            this.removeDateDate = message.getRemoveDate().format(DATE_FORMATTER);
            this.removeDateTime = message.getRemoveDate().format(TIME_FORMATTER);
        }
        this.description = Objects.requireNonNullElse(message.getDescription(), "");
    }

    public String getPublishDateDate() {
        return publishDateDate;
    }

    public void setPublishDateDate(String publishDateDate) {
        this.publishDateDate = Objects.requireNonNullElse(publishDateDate, "");
    }

    public String getPublishDateTime() {
        return publishDateTime;
    }

    public void setPublishDateTime(String publishDateTime) {
        this.publishDateTime = Objects.requireNonNullElse(publishDateTime, "");
    }

    public String getRemoveDateDate() {
        return removeDateDate;
    }

    public void setRemoveDateDate(String removeDateDate) {
        this.removeDateDate = Objects.requireNonNullElse(removeDateDate, "");
    }

    public String getRemoveDateTime() {
        return removeDateTime;
    }

    public void setRemoveDateTime(String removeDateTime) {
        this.removeDateTime = Objects.requireNonNullElse(removeDateTime, "");
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = Objects.requireNonNullElse(description, "");
    }

    public LocalDateTime getPublishDate() {
        return convert(publishDateDate, publishDateTime);
    }

    public LocalDateTime getRemoveDate() {
        return convert(removeDateDate, removeDateTime);
    }

    private LocalDateTime convert(String date, String time) {
        LocalDateTime result = null;
        try {
            result = LocalDateTime.parse(date + " " + time, DATE_TIME_FORMATTER);
        } catch (DateTimeParseException ignore) {
            // ignore
        }
        return result;
    }

}
