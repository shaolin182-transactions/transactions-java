package org.transactions.persistence.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.transactions.persistence.converters.DateToOffsetDateTimeConverter;
import org.transactions.persistence.converters.OffsetDateTimeToDateConverter;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class MongoConfig  {

    @Bean
    public MongoCustomConversions customConversions(){
        List<Converter<?,?>> converters = new ArrayList<>();
        converters.add(new DateToOffsetDateTimeConverter());
        converters.add(new OffsetDateTimeToDateConverter());
        return new MongoCustomConversions(converters);
    }
}
