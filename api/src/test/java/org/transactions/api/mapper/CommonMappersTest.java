package org.transactions.api.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class CommonMappersTest {

    public static Stream<Arguments> dataforFromDateToStringTest() {
        return Stream.of(
                Arguments.of(null, null),
                Arguments.of("2026-05-01T14:15:00Z", OffsetDateTime.of(2026, 5,1,14,15,0,0, ZoneOffset.UTC))

        );
    }

    public static Stream<Arguments> dataforFromStringToDateTest() {
        return Stream.of(
                Arguments.of(null, null),
                Arguments.of(null, ""),
                Arguments.of(null, "sledjfe"),
                Arguments.of(OffsetDateTime.of(2026, 5,1,14,15,0,0, ZoneOffset.UTC), "2026-05-01T14:15:00Z")

        );
    }

    @ParameterizedTest
    @MethodSource("dataforFromDateToStringTest")
    void fromDateToString(String expectedResult, OffsetDateTime date) {

        var commonMappers = new CommonMappers();
        var res = commonMappers.fromDateToString(date);

        Assertions.assertEquals(expectedResult, res);
    }

    @ParameterizedTest
    @MethodSource("dataforFromStringToDateTest")
    void fromStringToDate(OffsetDateTime expectedResult, String date) {

        var commonMappers = new CommonMappers();
        var res = commonMappers.fromStringToDate(date);

        Assertions.assertEquals(expectedResult, res);
    }
}
