package com.lab7.console;

import com.lab7.lib.Hello;
import com.lab7.lib.ActionHandler;

public class InnReservations
{
    public static void main(String[] args)
    {
        ActionHandler actHandler = new ActionHandler();

        System.out.println("Welcome to the Lab7 Inn Reservation System");
        System.out.println("0: View rooms and rates");
        System.out.println("1: Book a reservation");
        System.out.println("2: Alter a reservation");
        System.out.println("3: Cancel a reservation");
        System.out.println("4: View current reservations");
        System.out.println("5: View inn revenue information");
        System.out.println("Enter the number of the action you'd like to perform");


    }
}