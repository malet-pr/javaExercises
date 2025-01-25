package com.example;

public class SwitchPatternMatcherRecordPractice {

    /*
    Exercise: Vehicle Classification and Toll Calculation
    Write a method calculateToll(Vehicle vehicle) that:

    Classifies vehicles into different types such as:
    Car (with properties like numberOfPassengers).
    Truck (with properties like cargoWeight in tons).
    Motorcycle.

    Calculates toll fees based on:
    Cars: Base fee of $10 plus $2 per passenger.
    Trucks: Base fee of $20 plus $5 per ton of cargo weight.
    Motorcycles: Flat fee of $5.
    For any unknown vehicle type, return "Vehicle not recognized".

    Optional Challenge:
    Extend the exercise to handle "special vehicles" like Emergency Vehicles,
    which have a toll fee of $0, and Electric Vehicles, which receive a 50%
    discount on their calculated toll fee.
    */

    interface Toll {
        int calculateToll();
    }

    record Car(int seats,boolean isElectric,boolean isEmergency) implements Toll {
        @Override
        public int calculateToll() {
            return 2 + seats*4;
        }
    }
    record Truck(int cargoWeight,boolean isElectric) implements Toll {
        @Override
        public int calculateToll() {
            return cargoWeight*7;
        }
    }
    record Motorcycle(boolean isElectric,boolean isEmergency) implements Toll {
        @Override
        public int calculateToll() {
            return 5;
        }
    }

    public static int finalToll(int initialToll, boolean isElectric, boolean isEmergency){
        if(isEmergency) return 0;
        if(isElectric) return initialToll/2;
        return initialToll;
    }

    public static String calculateToll(Object vehicle) {
        return switch (vehicle) {
            case Car c -> "Car: Toll is $" + finalToll(c.calculateToll(),c.isElectric,c.isEmergency);
            case Truck t -> "Truck: Toll is $" + finalToll(t.calculateToll(),t.isElectric,false);
            case Motorcycle m -> "Motorcycle: Toll is $" + finalToll(m.calculateToll(),m.isElectric,m.isEmergency);
            default -> "unknown vehicle";
        };
    }

    public static void main(String[] args) {
        System.out.println(calculateToll(new Car(4,false,true)));
        System.out.println(calculateToll(new Truck(10,false)));
        System.out.println(calculateToll(new Motorcycle(true,false)));
        System.out.println(calculateToll("Bicycle"));

    }

}
