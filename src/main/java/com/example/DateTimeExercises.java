package com.example;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class DateTimeExercises {

    // JAVA 9
    /*
    ### LocalDate.datesUntil()
    - Write a method to generate a stream of dates from today until the end of the month using LocalDate.datesUntil.
    - Given two dates, use datesUntil to create a stream of all dates in that range and print them.
    */
    public static void datesUntilEndOfMonth(){
        Stream<LocalDate> daysUntil = LocalDate.now().datesUntil(YearMonth.now().atEndOfMonth().plusDays(1));
        daysUntil.forEach(System.out::println);
    }
    public static void datesBetweenTwo(LocalDate startDate, LocalDate endDate){
        Stream<LocalDate> daysUntil = startDate.datesUntil(endDate.plusDays(1));
        daysUntil.forEach(System.out::println);
    }

    /*
    ### Duration.toDaysPart(), toHoursPart(), toMinutesPart(), toSecondsPart()
    - Write a method that calculates the duration between two Instant objects and prints the number of days, hours,
      minutes, and seconds separately.
    */
    public static void durationBetween(String startDate, String endDate){
        ZoneId z = ZoneId.of("America/Buenos_Aires");
        LocalDateTime start = LocalDateTime.parse(startDate);
        LocalDateTime end = LocalDateTime.parse(endDate);
        Duration duration = Duration.between(start.atZone(z).toInstant(), end.atZone(z).toInstant());
        System.out.println(STR."There are \{duration.toDaysPart()} days, \{duration.toHoursPart()} hours, \{duration.toMinutesPart()} minutes, and \{duration.toSecondsPart()} seconds between those two dates.");
    }

    /*
    ### Period.between()
    - Write a method that calculates the period between two LocalDate objects and prints the number of years, months,
      and days.
    */
    public static void periodBetween(String startDate, String endDate){
        Period period = Period.between(LocalDate.parse(startDate), LocalDate.parse(endDate));
        System.out.println(STR."There are \{period.getYears()} years, \{period.getMonths()} months, and \{period.getDays()} days between those two dates.");
    }

    // JAVA 10
    /*
    ### LocalTime.toNanoOfDay() and LocalTime.ofNanoOfDay()
    - Write a method that converts a given LocalTime to its nanosecond-of-day representation using toNanoOfDay.
    - Given a nanosecond-of-day value, use ofNanoOfDay to create a LocalTime and print it.
    */
    public static long timeToNano(String time){
        LocalTime localTime = LocalTime.parse(time);
        return localTime.toNanoOfDay();
    }
    public static void nanoToTime(long time){
        System.out.println(LocalTime.ofNanoOfDay(time));
    }

    /*
    ### Optional enhancements with Date and Time
    - Write a method that finds the last day of the month from a given date and handles it using Optional to avoid
      null values.
    - Given an optional LocalDate, use ifPresent to print the date or a default message if the date is not present.
    */
    public static int lastDayOfMonth(String date){
        Optional<String> opt = Optional.ofNullable(date).filter(Predicate.not(String::isEmpty)).or(() -> Optional.of("2024-03-01"));
        LocalDate localDate = LocalDate.parse(opt.get());
        YearMonth yearMonth = YearMonth.of(localDate.getYear(), localDate.getMonthValue());
        return yearMonth.atEndOfMonth().getDayOfMonth();
    }
    public static void printIfPresent(Optional<LocalDate> date){
        if(date.isPresent()){
            System.out.println(date.get().toString());
        } else {
            System.out.println("No date found");
        }
    }

    // JAVA 11
    /*
    ### LocalDate.now(Clock)
    - Write a method that prints the current date using a specific time zone by creating a Clock instance.
    - Create a Clock that is offset from UTC and use it to get the current date.
    */
    public static void printTimeUsingTimeZone(String timezone){
        List<String> timeZones = Stream.of(TimeZone.getAvailableIDs()).toList();
        if(timeZones.contains(timezone)) {
            Clock clock = Clock.system(ZoneId.of(timezone));
            System.out.println(LocalDateTime.now(clock));
        } else {
            System.out.println("Timezone not found");
        }
    }
    public static void printTimeUsingOffsetUTC(){
        Clock baseClock = Clock.systemUTC();
        Clock clock = Clock.offset(baseClock, Duration.ZERO);
        System.out.println(LocalDateTime.now(clock));
    }

    /*
    ### Instant.now()
    - Write a method to get the current Instant and format it using DateTimeFormatter.ISO_INSTANT.
    - Given an Instant, use Instant.now to calculate the duration since that instant and print it.
    */
    public static void printCurrentInstant(){
        Instant instant = Instant.now();
        System.out.println(DateTimeFormatter.ISO_INSTANT.format(instant));
    }
    public static void printDurationFromInstant(Instant instant){
        Instant now = Instant.now();
        Duration duration = Duration.between(now, instant);
        System.out.println(STR."Days since that instant: \{-duration.toDaysPart()}");
    }

    /*
    ### LocalDate, LocalTime, and LocalDateTime Enhancements
    - Write a method that combines a LocalDate and a LocalTime to create a LocalDateTime and prints it.
    - Given a LocalDateTime, use toLocalDate and toLocalTime to extract the date and time parts separately and print
      them.
    */
    public static void combineDateTime(String dateStr, String timeStr){
        LocalDate date = LocalDate.parse(dateStr);
        LocalTime time = LocalTime.parse(timeStr);
        LocalDateTime dateTime = LocalDateTime.of(date, time);
        System.out.println(dateTime.toString());
    }
    public static void splitDateTime(String datetime){
        LocalDateTime dateTime = LocalDateTime.parse(datetime);
        LocalDate date = dateTime.toLocalDate();
        LocalTime time = dateTime.toLocalTime();
        System.out.println(STR."Date: \{date} - Time: \{time}");
    }

    /*
    ### Parsing and Formatting
    - Write a method that parses a date string in the format "dd-MM-yyyy" to a LocalDate using DateTimeFormatter.
    - Given a LocalDate, format it to a string in the format "MMMM dd, yyyy" using DateTimeFormatter.
    */
    public static LocalDate parseDate(String dateStr){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return LocalDate.parse(dateStr, dtf);
    }
    public static String dateToString(LocalDate localDate){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
        return localDate.format(dtf);
    }

    /*
    ### Adjusters and Queries
    - Write a method that uses TemporalAdjusters to find the next Monday from a given date.
    - Given a LocalDate, use a custom temporal query to check if the date is a weekend (Saturday or Sunday).
    */
    public static LocalDate nextMonday(){
        TemporalAdjuster temporalAdjuster = TemporalAdjusters.next(DayOfWeek.MONDAY);
        return LocalDate.now().with(temporalAdjuster);
    }
    public static Boolean isWeekend(LocalDate date){
        int dayOfWeek = date.getDayOfWeek().getValue();
        if(dayOfWeek == 6 || dayOfWeek == 7){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    // GENERAL DATE AND TIME PRACTICE

    /*
    ### ZonedDateTime
    - Write a method to get the current date and time in a specific time zone (e.g., "America/New_York") using
      ZonedDateTime.
    - Given a ZonedDateTime, convert it to another time zone and print both the original and converted date-times.
    */
    public static ZonedDateTime toZonedDateTime(String timezone){
        List<String> timeZones = Stream.of(TimeZone.getAvailableIDs()).toList();
        ZonedDateTime zdt = null;
        if(timeZones.contains(timezone)) {
            Clock clock = Clock.system(ZoneId.of(timezone));
            zdt = ZonedDateTime.of(LocalDateTime.now(clock), ZoneId.of(timezone));
        } else {
            System.out.println("Timezone not found");
        }
        return zdt;
    }
    public static void changeTimeZone(String date, String timezone){
        LocalDateTime ldt = LocalDateTime.parse(date);
        List<String> timeZones = Stream.of(TimeZone.getAvailableIDs()).toList();
        if(timeZones.contains(timezone)) {
            ZonedDateTime zdt1 = ZonedDateTime.of(ldt, ZoneId.systemDefault());
            ZonedDateTime zonedUTC = ldt.atZone(ZoneId.of("UTC"));
            ZonedDateTime zdt2 = zonedUTC.withZoneSameInstant(ZoneId.of(timezone));
            System.out.println(STR."Original: \{zdt1} \nConverted: \{zdt2}");
        }
    }

    /*
    ### ChronoUnit
    - Write a method that uses ChronoUnit to calculate the number of days between two LocalDate objects.
    - Given two Instant objects, use ChronoUnit to calculate the number of hours and minutes between them.
    */


    public static void main(String[] args) {
        /*
        //datesUntilEndOfMonth();
        //datesBetweenTwo(LocalDate.now().plusWeeks(1), LocalDate.now().plusWeeks(2).plusDays(3));
        //durationBetween("2024-06-02T08:12:10","2024-06-27T10:15:30");
        //periodBetween("2020-01-07","2024-06-27");
        //System.out.println(timeToNano("08:12:10"));
        //nanoToTime(timeToNano("08:12:10"));
        //System.out.println(lastDayOfMonth(""));
        //System.out.println(lastDayOfMonth(LocalDate.now().toString()));
        //printIfPresent(Optional.empty());
        //printIfPresent(Optional.of(LocalDate.now()));
        //printTimeUsingTimeZone("America/Buenos_Aires");
        //printTimeUsingTimeZone("Europe/Paris");
        //printTimeUsingTimeZone("Asia/Calcutta");
        //printTimeUsingTimeZone("any time zone");
        //printTimeUsingOffsetUTC();
        //printCurrentInstant();
        //printDurationFromInstant(Instant.parse("2024-06-01T10:15:30.00Z"));
        //combineDateTime("2024-06-01","10:15:30.00");
        //splitDateTime("2024-06-01T10:15:30.00");
        //System.out.println(parseDate("11-03-2024"));
        //System.out.println(dateToString(LocalDate.now().minusDays(4)));
        //System.out.println(isWeekend(LocalDate.now().minusDays(1)));
        //System.out.println(nextMonday());
        System.out.println(toZonedDateTime("America/Buenos_Aires"));
        System.out.println(toZonedDateTime("Europe/Berlin"));
        System.out.println(toZonedDateTime("fruta"));
        */
        //changeTimeZone("2024-03-05T17:31:00", "Asia/Shanghai");

    }

}
