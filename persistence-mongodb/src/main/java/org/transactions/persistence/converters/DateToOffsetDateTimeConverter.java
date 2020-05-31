package org.transactions.persistence.converters;

import org.springframework.core.convert.converter.Converter;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * As MongoDB does not handle OffsetDateTime object, we must convert it into a Date object
 * Example : https://www.baeldung.com/spring-data-mongodb-zoneddatetime
 */
public class DateToOffsetDateTimeConverter implements Converter<Date, OffsetDateTime> {

    /**
     * Convert a Date object into a OffsetDateTime object
     * @param source : current date
     * @return date in OffsetDateTime format
     */
    @Override
    public OffsetDateTime convert(Date source) {
        return source == null ? null : OffsetDateTime.ofInstant(source.toInstant(), ZoneId.systemDefault());
    }
}
