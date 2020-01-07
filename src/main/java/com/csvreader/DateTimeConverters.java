package com.csvreader;

import java.util.Optional;

public class DateTimeConverters {

    public static java.time.LocalDate jodaLocalDateToJavaLocalDate(org.joda.time.LocalDate localDate) {
        return java.time.LocalDate.of(localDate.getYear(), localDate.getMonthOfYear(), localDate.getDayOfMonth());
    }

    public static org.joda.time.LocalDate javaLocalDateToJodaLocalDate(java.time.LocalDate localDate) {
        return new org.joda.time.LocalDate(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth());
    }

    public static java.time.LocalTime jodaLocalTimeToJavaLocalTime(org.joda.time.LocalTime localTime) {
        return java.time.LocalTime.of(
                localTime.getHourOfDay(),
                localTime.getMinuteOfHour(),
                localTime.getSecondOfMinute(),
                localTime.getMillisOfSecond() * 1000000);
    }

    public static org.joda.time.LocalTime javaLocalTimeToJodaLocalTime(java.time.LocalTime localTime) {
        return new org.joda.time.LocalTime(
                localTime.getHour(),
                localTime.getMinute(),
                localTime.getSecond(),
                localTime.getNano() / 1000000);
    }

    public static java.time.LocalDateTime jodaLocalDateTimeToJavaLocalDateTime(org.joda.time.LocalDateTime localDateTime) {
        return java.time.LocalDateTime.of(
                localDateTime.getYear(),
                localDateTime.getMonthOfYear(),
                localDateTime.getDayOfMonth(),
                localDateTime.getHourOfDay(),
                localDateTime.getMinuteOfHour(),
                localDateTime.getSecondOfMinute(),
                localDateTime.getMillisOfSecond() * 1000000);
    }

    public static org.joda.time.LocalDateTime javaLocalDateTimeToJodaLocalDateTime(java.time.LocalDateTime localDateTime) {
        return new org.joda.time.LocalDateTime(
                localDateTime.getYear(),
                localDateTime.getMonthValue(),
                localDateTime.getDayOfMonth(),
                localDateTime.getHour(),
                localDateTime.getMinute(),
                localDateTime.getSecond(),
                localDateTime.getNano() / 1000000);
    }

    public static java.time.MonthDay jodaMonthDayToJavaMonthDay(org.joda.time.MonthDay monthDay) {
        return java.time.MonthDay.of(monthDay.getMonthOfYear(), monthDay.getDayOfMonth());
    }

    public static org.joda.time.MonthDay javaMonthDayToJodaMonthDay(java.time.MonthDay monthDay) {
        return new org.joda.time.MonthDay(monthDay.getMonthValue(), monthDay.getDayOfMonth());
    }

    public static java.time.YearMonth jodaYearMonthToJavaYearMonth(org.joda.time.YearMonth yearMonth) {
        return java.time.YearMonth.of(yearMonth.getYear(), yearMonth.getMonthOfYear());
    }

    public static org.joda.time.YearMonth javaYearMonthToJodaYearMonth(java.time.YearMonth yearMonth) {
        return new org.joda.time.YearMonth(yearMonth.getYear(), yearMonth.getMonthValue());
    }


    public static java.time.ZonedDateTime jodaDateTimeToJavaZonedDateTime(org.joda.time.DateTime dateTime) {
        return jodaInstantToJavaInstant(dateTime.toInstant()).atZone(jodaDateTimeZoneToJavaZoneId(dateTime.getZone()));
    }

    public static org.joda.time.DateTime javaZonedDateTimeToJodaDateTime(java.time.ZonedDateTime dateTime) {
        return new org.joda.time.DateTime(javaInstantToJodaInstant(dateTime.toInstant()), javaZoneIdToJodaDateTimeZone(dateTime.getZone()));
    }

    public static java.time.Instant jodaInstantToJavaInstant(org.joda.time.Instant instant) {
        return java.time.Instant.ofEpochMilli(instant.getMillis());
    }

    public static org.joda.time.Instant javaInstantToJodaInstant(java.time.Instant instant) {
        return new org.joda.time.Instant(instant.toEpochMilli());
    }

    public static java.time.ZoneId jodaDateTimeZoneToJavaZoneId(org.joda.time.DateTimeZone timeZone) {
        return java.time.ZoneId.of(timeZone.getID());
    }

    public static org.joda.time.DateTimeZone javaZoneIdToJodaDateTimeZone(java.time.ZoneId timeZone) {
        return org.joda.time.DateTimeZone.forID(timeZone.getId());
    }

    public static java.time.Duration jodaDurationToJavaDuration(org.joda.time.Duration duration) {
        return java.time.Duration.ofMillis(duration.getMillis());
    }

    public static org.joda.time.Duration javaDurationToJodaDuration(java.time.Duration duration) {
        return org.joda.time.Duration.millis(duration.toMillis());
    }

    public static java.time.Period jodaPeriodToJavaPeriod(org.joda.time.Period period) {
        return java.time.Period.of(period.getYears(), period.getMonths(), period.getDays());
    }

    public static org.joda.time.Period javaPeriodToJodaPeriod(java.time.Period period) {
        return new org.joda.time.Period(period.getYears(), period.getMonths(), 0, period.getDays(), 0, 0, 0, 0);
    }

    public static Optional<String> safelyStringifyJodaDateTimeStringToNominalDateString(String jodaTimeStr) {
        try {
            String inputPattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZZ";
            org.joda.time.format.DateTimeFormatter formatter1 = org.joda.time.format.DateTimeFormat.forPattern(inputPattern);
            org.joda.time.DateTime dateTime = formatter1.parseDateTime(jodaTimeStr);

            String outputPattern = "MM/dd/yyyy";
            org.joda.time.format.DateTimeFormatter formatter2 = org.joda.time.format.DateTimeFormat.forPattern(outputPattern);
            return Optional.ofNullable(dateTime.toString(formatter2));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public static Optional<java.time.LocalDateTime> stringifyJodaDateTimeStringToLocalDateTimeSafelyWithoutTimezone(String jodaDateTime) {
        try {
            java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter
                    .ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");
            //.withZone(ZoneId.of("UTC"));
            java.time.LocalDateTime dateTime = java.time.LocalDateTime.parse(jodaDateTime, formatter);
            return Optional.ofNullable(dateTime);
        } catch (Exception e) {
            return Optional.empty();
        }

    }
}
