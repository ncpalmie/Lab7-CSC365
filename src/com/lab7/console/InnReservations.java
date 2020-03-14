package com.lab7.console;

import com.lab7.lib.Hello;
import com.lab7.lib.ActionHandler;
import java.util.Scanner;
import java.util.ArrayList;

public class InnReservations
{
    public static void main(String[] args)
    {
        int userInput = 0;
        String outputString;
        ArrayList<String> argsList = new ArrayList<String>();
        ActionHandler actHandler = new ActionHandler();
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

            userInput = instream.nextInt();

            if (userInput == 1) {
                //Prompt for reservation information and fill argsList
            } else if (userInput == 2) {
                //Prompt for reservation code and changes to make to fill argsList
            } else if (userInput == 3) {
                //Prompt for reservation code
            } else if (userInput == 4) {
                //Prompt for reservation information and fill argsList
            }
            else if (userInput == 6) {
                break;
            }

            outputString = actHandler.handleAction(userInput, argsList);

            if (actHandler.getActionResult() == ActionHandler.Results.SUCCESS ||
                    actHandler.getActionResult() == ActionHandler.Results.FAIL) {
                System.out.println(outputString);
            } else {
                while (actHandler.getActionResult() == ActionHandler.Results.PROMPT_AGAIN) {
                    System.out.println(outputString);
                    argsList = new ArrayList<String>();

                    //Display action specific prompts and fill argsList appropriately

                    actHandler.handleAction(-1, argsList);
                }
            }
        }
        System.out.println("Goodbye.");
    }
}