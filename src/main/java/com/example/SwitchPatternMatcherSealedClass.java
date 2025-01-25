package com.example;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

public class SwitchPatternMatcherSealedClass {

    /*
    Requirements:

    Sealed Hierarchy for User Types:
    Use a sealed class called User with the following subclasses:
    Admin (has permissions for everything).
    Manager (has permissions to view and edit).
    Employee (has permission to view only).
    Guest (no permissions).

    Permissions Interface:
    Define an interface Permissions with a method List<String> getPermissions().
    Implement the Permissions interface in each subclass to return a list of permissions based
    on the user type:
    Admin: ["View", "Edit", "Delete"]
    Manager: ["View", "Edit"]
    Employee: ["View"]
    Guest: []

    Switch Expression:
    Write a method describeUserPermissions(User user) that uses a switch expression to return a string
    describing the user's permissions.

    Optional Challenge:
    Add a method canPerformAction(User user, String action) that checks if a specific user can perform
    a given action (e.g., "Edit" or "Delete").
    */

    enum Permit {
        VIEW, EDIT, DELETE
    }
    @Data
    public static abstract sealed class User permits Admin, Manager, Employee, Guest {
        boolean canPerformAction(Permit action){
            return false;
        }
    }
    public sealed interface Permissions permits Admin, Manager, Employee, Guest {
        default List<Permit> getPermissions(){
            return List.of();
        }
    }
    public static final class Admin extends User implements Permissions {
        @Override
        public List<Permit> getPermissions(){
            return List.of(Permit.VIEW, Permit.EDIT, Permit.DELETE);
        }
        @Override
        public boolean canPerformAction(Permit action) {
            return this.getPermissions().contains(action);
        }
    }
    public static final class Manager extends User implements Permissions {
        @Override
        public List<Permit> getPermissions(){
            return List.of(Permit.VIEW, Permit.EDIT);
        }
        @Override
        public boolean canPerformAction(Permit action) {
            return this.getPermissions().contains(action);
        }
    }
    public static final class Employee extends User implements Permissions {
        @Override
        public List<Permit> getPermissions(){
            return List.of(Permit.VIEW);
        }
        @Override
        public boolean canPerformAction(Permit action) {
            return this.getPermissions().contains(action);
        }
    }
    public static final class Guest extends User implements Permissions {
    }
    public static String describeUserPermissions(Object user) {
        List<Permit> permits = switch (user){
            case Admin a -> a.getPermissions();
            case Manager m -> m.getPermissions();
            case Employee e -> e.getPermissions();
            case Guest g -> g.getPermissions();
            default -> List.of();
        };
        String perm = !permits.isEmpty() ? ": Permissions are " + permits : ": No permissions";
        return user.getClass().getSimpleName() + perm;
    }
    public static String canPerformAction(User user, Permit action) {
        boolean res = switch (user){
            case Admin a -> a.canPerformAction(action);
            case Manager m -> m.canPerformAction(action);
            case Employee e -> e.canPerformAction(action);
            case Guest g -> g.canPerformAction(action);
        };
        return user.getClass().getSimpleName() + " can " + action + "? " + res;
    }

    public static void main(String[] args) {
        System.out.println(describeUserPermissions(new Admin()));
        System.out.println(describeUserPermissions(new Manager()));
        System.out.println(describeUserPermissions(new Employee()));
        System.out.println(describeUserPermissions(new Guest()));
        System.out.println(canPerformAction(new Manager(), Permit.DELETE));
        System.out.println(canPerformAction(new Employee(), Permit.VIEW));
        System.out.println(canPerformAction(new Guest(), Permit.VIEW));
    }


}
