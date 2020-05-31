package org.transactions.persistence.converters;

import org.springframework.core.convert.converter.Converter;

import java.time.OffsetDateTime;
import java.util.Date;

/**
 * As MongoDB does not handle OffsetDateTime object, we must convert it into a Date object
 * Example : https://www.baeldung.com/spring-data-mongodb-zoneddatetime
 */
public class OffsetDateTimeToDateConverter implements Converter<OffsetDateTime, Date> {

    /**
     * Convert an OffsetDateTime object into a Date object
     * @param source : the curent offsetDateTime
     * @return a date
     */
    @Override
    public Date convert(OffsetDateTime source) {
        return source == null ? null : Date.from(source.toInstant());
    }
}
