package info.saladlam.example.spring.noticeboard.support;

import java.beans.PropertyEditorSupport;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalQuery;

import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

// copy from org.springframework.beans.propertyeditors.CustomDateEditor
public class CustomJava8TimeEditor<T> extends PropertyEditorSupport {

	private final DateTimeFormatter dateTimeFormatter;
	private final TemporalQuery<T> query;
	private final boolean allowEmpty;
	private final int exactDateLength;

	public CustomJava8TimeEditor(DateTimeFormatter dateTimeFormatter, TemporalQuery<T> query, boolean allowEmpty) {
		this.dateTimeFormatter = dateTimeFormatter;
		this.query = query;
		this.allowEmpty = allowEmpty;
		this.exactDateLength = -1;
	}

	public CustomJava8TimeEditor(DateTimeFormatter dateTimeFormatter, TemporalQuery<T> query, boolean allowEmpty,
			int exactDateLength) {
		this.dateTimeFormatter = dateTimeFormatter;
		this.query = query;
		this.allowEmpty = allowEmpty;
		this.exactDateLength = exactDateLength;
	}

	@Override
	public void setAsText(@Nullable String text) throws IllegalArgumentException {
		if (this.allowEmpty && !StringUtils.hasText(text)) {
			// Treat empty String as null value.
			setValue(null);
		} else if (text != null && this.exactDateLength >= 0 && text.length() != this.exactDateLength) {
			throw new IllegalArgumentException(
					"Could not parse date: it is not exactly" + this.exactDateLength + "characters long");
		} else {
			try {
				setValue(this.dateTimeFormatter.parse(text, this.query));
			} catch (DateTimeParseException ex) {
				throw new IllegalArgumentException("Could not parse date: " + ex.getMessage(), ex);
			}
		}
	}

	@Override
	public String getAsText() {
		TemporalAccessor value = (TemporalAccessor) getValue();
		return (value != null? this.dateTimeFormatter.format(value): "");
	}

}
