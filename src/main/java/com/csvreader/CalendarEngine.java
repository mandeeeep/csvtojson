package com.csvreader;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.Locale;
import java.util.Optional;
import java.util.TimeZone;

public class CalendarEngine {

    public static void main(String args[]) {
        Optional<String> preparedFilters = Optional.ofNullable("DEF");
        Optional<String> filters = Optional.ofNullable("ABC");
        Optional<String> finalFilter = filters
                .map(x -> preparedFilters
                        .map(y -> x + "&" + y))
                .orElse(preparedFilters);
        System.out.println(finalFilter);
    }

    public static Optional<String> prepareFilter(Locale locale,
                                                 LocalDate date,
                                                 CalendarViewSelectedType view,
                                                 Optional<TimeZone> timeZoneSent,
                                                 String startLabel,
                                                 Optional<String> endLabel) {
        //We have added a try catch block on the whole code as,
        //in here there are a lot of experimental code base,
        //besides the context used are Locale, TimeZones which aren't always provided by the
        //client. So, we are defaulting to UTC, which isn't always the correct approach
        try {
            //First removing all entries
            //Now we load entries based on the filters available and date/time selection
            //First we retrieve Day of Week, Day of Month, Month of Year and Year
            DayOfWeek dayOfWeek = date.getDayOfWeek();
            int dayOfMonth = date.getDayOfMonth();
            Month month = date.getMonth();
            int year = date.getYear();
            //Will default to UTC if timezone isn't provided
            //this logic should not be used on mission critical piece of code
            //for this particular scenario we can consider its use case safe, until further improvements
            ZoneId zoneId = timeZoneSent.map(x -> x.toZoneId()).orElse(ZoneId.of("UTC"));
            DateTimeZone tmz = DateTimeZone.UTC;
            Optional<String> finalQuery = Optional.empty();
            try {
                tmz = DateTimeZone.forID(zoneId.getId());
            } catch (Exception e) {
            }

            switch (view) {
                case DAY: {
                    //Since we are preparing for a day
                    ZonedDateTime startWz = date.atStartOfDay().atZone(zoneId);
                    ZonedDateTime endWz = date.atTime(LocalTime.MAX).atZone(zoneId);
                    DateTime start = new DateTime(startWz.toInstant().toEpochMilli(), tmz);
                    DateTime end = new DateTime(endWz.toInstant().toEpochMilli(), tmz);
                    finalQuery = Optional.ofNullable(startLabel + "=(" + x(start) + "," + x(end) + ")" + endLabel.map(x ->
                            "&" + x + "=(" + x(start) + "," + x(end) + ")"
                    ).orElse(""));
                    break;
                }
                case WEEK: {
                    //We are preparing for the week
                    final DayOfWeek firstDayOfWeek = WeekFields.of(locale).getFirstDayOfWeek();
                    final DayOfWeek lastDayOfWeek = DayOfWeek.of(((firstDayOfWeek.getValue() + 5) % DayOfWeek.values().length) + 1);
                    ZonedDateTime startWz = date.atStartOfDay()
                            .with(TemporalAdjusters.previousOrSame(firstDayOfWeek))
                            .atZone(zoneId);
                    ZonedDateTime endWz = date.atTime(LocalTime.MAX)
                            .with(TemporalAdjusters.nextOrSame(lastDayOfWeek))
                            .atZone(zoneId);
                    DateTime start = new DateTime(startWz.toInstant().toEpochMilli(), tmz);
                    DateTime end = new DateTime(endWz.toInstant().toEpochMilli(), tmz);
                    finalQuery = Optional.ofNullable(startLabel + "=(" + x(start) + "," + x(end) + ")" + endLabel.map(x ->
                            "&" + x + "=(" + x(start) + "," + x(end) + ")"
                    ).orElse(""));
                    break;
                }
                case MONTH: {
                    //We are preparing for the Month
                    ZonedDateTime startMz = date.atStartOfDay()
                            .withDayOfMonth(1)
                            .atZone(zoneId);
                    ZonedDateTime endMz = date.atTime(LocalTime.MAX)
                            .withDayOfMonth(date.lengthOfMonth())
                            .atZone(zoneId);
                    DateTime start = new DateTime(startMz.toInstant().toEpochMilli(), tmz);
                    DateTime end = new DateTime(endMz.toInstant().toEpochMilli(), tmz);
                    finalQuery = Optional.ofNullable(startLabel + "=(" + x(start) + "," + x(end) + ")" + endLabel.map(x ->
                            "&" + x + "=(" + x(start) + "," + x(end) + ")"
                    ).orElse(""));
                    break;
                }
                case YEAR: {
                    //We are preparing for the Year
                    ZonedDateTime startMz = date.atStartOfDay()
                            .withDayOfYear(1)
                            .atZone(zoneId);
                    ZonedDateTime endMz = date.atTime(LocalTime.MAX)
                            .withDayOfYear(date.lengthOfYear())
                            .atZone(zoneId);
                    DateTime start = new DateTime(startMz.toInstant().toEpochMilli(), tmz);
                    DateTime end = new DateTime(endMz.toInstant().toEpochMilli(), tmz);
                    System.out.println("==>" + start + "," + end);
                    finalQuery = Optional.ofNullable(startLabel + "=(" + x(start) + "," + x(end) + ")" + endLabel.map(x ->
                            "&" + x + "=(" + x(start) + "," + x(end) + ")"
                    ).orElse(""));
                    break;
                }
            }
            return finalQuery;
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public static String x(DateTime dateTime) {
        String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZZ";
        DateTimeFormatter formatter = DateTimeFormat.forPattern(pattern);
        return dateTime.toString(formatter);
    }

    public static LocalDate y(DateTime dateTime) {
        DateTime dateTimeUtc = dateTime.withZone(DateTimeZone.UTC);
        return LocalDate.of(dateTimeUtc.getYear(), dateTimeUtc.getMonthOfYear(), dateTimeUtc.getDayOfMonth());
    }

    public static LocalDate z(String jodaTimeStr) {
        String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZZ";
        DateTimeFormatter formatter = DateTimeFormat.forPattern(pattern);
        return y(formatter.parseDateTime(jodaTimeStr));
    }


}
