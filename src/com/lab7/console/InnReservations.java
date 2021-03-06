package com.lab7.console;

import com.lab7.lib.ActionHandler;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class InnReservations
{
    public static void main(String[] args)
    {
        int userInput = 0;
        String outputString;
        ArrayList<String> argsList = new ArrayList<String>();
        ActionHandler actHandler = new ActionHandler();
        String userInputStr;
        Scanner instream = new Scanner(System.in);

        System.out.println("Welcome to the Lab7 Inn Reservation System");
        while (userInput != 6) {
            System.out.println("0: View rooms and rates");
            System.out.println("1: Book a reservation");
            System.out.println("2: Alter a reservation");
            System.out.println("3: Cancel a reservation");
            System.out.println("4: View current reservation");
            System.out.println("5: View inn revenue information");
            System.out.println("6: Exit reservation system");
            System.out.print("Enter the number of your desired action: ");

            userInputStr = instream.nextLine();
            while (!Pattern.matches("[0-9]+", userInputStr) || Integer.parseInt(userInputStr) > 6 ||
                    Integer.parseInt(userInputStr) < 0) {
                System.out.println("Not a valid input, please try again");
                userInputStr = instream.nextLine();
            }

            userInput = Integer.parseInt(userInputStr);

            System.out.println();
            
            if (userInput == 1) {
                ConsoleUtils.getReservationInformation(argsList, instream);
            } else if (userInput == 2) {
                ConsoleUtils.getAlterationInformation(argsList, instream);
            } else if (userInput == 3) {
                //Prompt for reservation code
                ConsoleUtils.getReservationCode(argsList, instream);
            } else if (userInput == 4) {
                //Prompt for reservation information and fill argsList
                ConsoleUtils.getReservationSearch(argsList, instream);
            }
            else if (userInput == 6) {
                break;
            }

            outputString = actHandler.handleAction(userInput, argsList);

            while (actHandler.getActionResult() == ActionHandler.Results.PROMPT_AGAIN) {
                System.out.println(outputString);
                argsList = new ArrayList<String>();

                //Display action specific prompts and fill argsList appropriately
                if (userInput == 1) {
                    if (outputString.charAt(0) == 'Y') {
                        ConsoleUtils.confirmReservation(argsList, instream);
                    }
                    else if (outputString.charAt(0) == 'P' || outputString.charAt(0) == 'U') {
                        ConsoleUtils.getOptionChoice(argsList, instream);
                    }
                }
                else if (userInput == 2) {
                    ConsoleUtils.confirmReservation(argsList, instream);
                }
                else if (userInput == 3) {
                    ConsoleUtils.confirmCancellation(argsList, instream);
                }

                outputString = actHandler.handleAction(-1, argsList);
            }
            System.out.println(outputString);
        }

        instream.close();
        System.out.println("Goodbye.");
    }
}