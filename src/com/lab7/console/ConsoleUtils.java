package com.lab7.console;

import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;

public class ConsoleUtils {
    public static List<String> getReservationInformation(List<String> argsList, Scanner instream) {

        argsList.clear();

        //Order of entries based on order in spec for FR2 input
        System.out.println("Please enter the following information to complete\n" +
                "your reservation request. If you have no preference for an\n" +
                "option, leave the entry blank and press enter:");

        System.out.print("Enter your first name: ");
        argsList.add(instream.nextLine());
        System.out.print("Enter your last name: ");
        argsList.add(instream.nextLine());
        System.out.print("Enter a room code: ");
        argsList.add(instream.nextLine());
        System.out.print("Enter a bed type (Queen, King, or Double): ");
        argsList.add(instream.nextLine());
        System.out.print("Enter the start date of the reservation (YYYY-MM-DD): ");
        argsList.add(instream.nextLine());
        System.out.print("Enter the end date of the reservation (YYYY-MM-DD): ");
        argsList.add(instream.nextLine());
        System.out.print("Enter the number of children staying: ");
        argsList.add(instream.nextLine());
        System.out.print("Enter the number of adults staying: ");
        argsList.add(instream.nextLine());
        System.out.println();

        return argsList;
    }

    public static List<String> confirmReservation(List<String> argsList, Scanner instream) {
        argsList.clear();

        System.out.print("Enter \'y\' to confirm your reservation or \'n\' to cancel it: ");
        argsList.add(instream.nextLine());
        System.out.println();

        return argsList;
    }
}
