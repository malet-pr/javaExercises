package com.example;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

public class RecordPractice {
    record Person(String name, LocalDate birthday) {}

    record Data(Person person, Long age) {}

    public static Long computeAgeInDays(Person person) {
        return ChronoUnit.DAYS.between(LocalDate.now(), person.birthday);
    }

    public static int compareByAgeThenName(Data d1, Data d2){
        if(!Objects.equals(d1.age(), d2.age())){
            return Long.compare(d1.age(), d2.age());
        } else {
            return (d1.person().name()).compareToIgnoreCase(d2.person().name());
        }
    }

    public static List<Person> sortPeopleByAge(List<Person> people) {
        return people.stream()
                .map(person -> new Data(person, computeAgeInDays(person)))
                .sorted(RecordPractice::compareByAgeThenName)
                .map(Data::person)
                .toList();
    }

    public static void main(String[] args) {
        var personList = List.of(
                new Person("Peter", LocalDate.of(2020, 9, 11)),
                new Person("Carl", LocalDate.of(2020, 9, 11)),
                new Person("John", LocalDate.of(1970, 4, 1)),
                new Person("Ann", LocalDate.of(1970, 3, 1)),
                new Person("Mary", LocalDate.of(1990, 1, 31))
        );
        var people = sortPeopleByAge(personList);
        people.forEach(System.out::println);
    }
}

