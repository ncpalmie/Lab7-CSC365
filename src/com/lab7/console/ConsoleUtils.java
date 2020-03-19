package com.lab7.console;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
        while (argsList.get(5).compareTo(argsList.get(4)) <= 0) {
            System.out.print("End date must be after start date, try again: ");
            argsList.set(5, instream.nextLine());
        }
        System.out.print("Enter the number of children staying: ");
        argsList.add(instream.nextLine());
        System.out.print("Enter the number of adults staying: ");
        argsList.add(instream.nextLine());
        System.out.println();

        return argsList;
    }

    public static List<String> getAlterationInformation(List<String> argsList, Scanner instream) {
        argsList.clear();

        System.out.print("Enter the reservation code of the reservation you want to alter: ");
        argsList.add(instream.nextLine());

        System.out.println("Please enter the following information to complete\n" +
                "your alteration request. If you have no change for an\n" +
                "option, leave the entry blank and press enter:");

        System.out.print("Enter the new first name: ");
        argsList.add(instream.nextLine());
        System.out.print("Enter the new last name: ");
        argsList.add(instream.nextLine());
        System.out.print("Enter the new check in date (YYYY-MM-DD): ");
        argsList.add(instream.nextLine());
        System.out.print("Enter the new check out date (YYYY-MM-DD): ");
        argsList.add(instream.nextLine());
        while (argsList.get(3).compareTo(argsList.get(2)) <= 0) {
            System.out.println("End date must be after start date, try again: ");
            argsList.set(3, instream.nextLine());
        }
        System.out.print("Enter the new number of children staying: ");
        argsList.add(instream.nextLine());
        System.out.print("Enter the new number of adults staying: ");
        argsList.add(instream.nextLine());
        System.out.println();

        return argsList;
    }

    public static List<String> getReservationSearch(List<String> argsList, Scanner instream) {
        argsList.clear();

        System.out.println("Fill out the following prompt to search for reservations. If you\n" +
                "have no specifier for a category, leave it blank to represent \"Any\".\n" +
                "Wildcards are permitted for non-date entries with the % character.");

        System.out.print("Enter a first name: ");
        argsList.add(instream.nextLine());
        System.out.print("Enter a last name: ");
        argsList.add(instream.nextLine());
        System.out.print("Enter a start date: ");
        argsList.add(instream.nextLine());
        System.out.print("Enter an end date: ");
        argsList.add(instream.nextLine());
        System.out.print("Enter a room code: ");
        argsList.add(instream.nextLine());
        System.out.print("Enter a reservation code: ");
        argsList.add(instream.nextLine());
        System.out.println();

        return argsList;
    }

    public static List<String> getReservationCode(ArrayList<String> argsList)
    {
        Scanner instream = new Scanner(System.in);

        argsList.clear();

        System.out.print("\nPlease enter the reservation code of the reservation to be deleted: ");
        argsList.add(instream.nextLine());

        instream.close();

        return argsList;
    }
    
    public static List<String> confirmReservation(List<String> argsList, Scanner instream) {
        argsList.clear();

        System.out.print("Enter \'y\' to confirm your new reservation or \'n\' to cancel it: ");
        argsList.add(instream.nextLine());
        System.out.println();

        return argsList;
    }

    public static List<String> getOptionChoice(List<String> argsList, Scanner instream) {
        argsList.clear();

        System.out.print("Enter the number of the room you'd like to book, or enter\n" +
                "\'c\' to cancel the request and return to main menu: ");
        argsList.add(instream.nextLine());
        System.out.println();

        return argsList;
    }

    public static int getNumWeekdays(String startDateStr, String endDateStr) {
        int retVal = 0;
        SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        Date startDate = null;
        try {
            startDate = dFormat.parse(startDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        calendar.setTime(startDate);
        while (!dFormat.format(calendar.getTime()).equals(endDateStr)) {
            if (!(calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) &&
                    !(calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)) {
                retVal++;
            }
            calendar.add(Calendar.DATE, 1);
        }

        return retVal;
    }

    public static int getNumDays(String startDateStr, String endDateStr) {
        int retVal = 0;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = null;
        try {
            startDate = dFormat.parse(startDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        calendar.setTime(startDate);
        while (!dFormat.format(calendar.getTime()).equals(endDateStr)) {
            retVal++;
            calendar.add(Calendar.DATE, 1);
        }
        return retVal;
    }
}
