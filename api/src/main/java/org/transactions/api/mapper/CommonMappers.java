package org.transactions.api.mapper;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Component
public class CommonMappers {

    public String fromDateToString(OffsetDateTime date) {
        if (date == null) {
            return null;
        }
        return date.format(DateTimeFormatter.ISO_DATE_TIME);
    }

    public OffsetDateTime fromStringToDate(String date) {
        if (StringUtils.isEmpty(date)) {
            return null;
        }

        try {
            return OffsetDateTime.parse(date, DateTimeFormatter.ISO_DATE_TIME);
        } catch (DateTimeParseException e){
            return null;
        }
    }
}
