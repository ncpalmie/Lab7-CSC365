package com.lab7.lib;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Action handler for actions created by console/gui user inputs
 */
public class ActionHandler {
    Action currentAction;
    Results actionResult;
    List<String> prevArgs;
    boolean continueAction = false;


    public enum Action {
        VIEW_ROOMS,
        CREATE_RES,
        ALTER_RES,
        CANCEL_RES,
        VIEW_RES,
        VIEW_REVENUE
    }

    /**
     * SUCCESS when action is done and return string contains final output
     *     information for action
     * PROMPT_AGAIN when action requires more user input to complete and
     *     return string is a new prompt to return to console/GUI
     * FAIL when action cannot complete properly and return string contains
     *     error information for user to correct
     */
    public enum Results {
        SUCCESS,
        PROMPT_AGAIN,
        FAIL
    }

    public ActionHandler() {
    }

    /**
     * Takes user input and handles desired action
     *
     * @param actionNumber The desired action number entered by the user in the
     *                     console/associated number from button press in GUI
     *
     * @return Formatted string containing desired information from database or
     * action status for user
     */
    public String handleAction(int actionNumber, List<String> argsList) {
        if (actionNumber != -1) {
            this.currentAction = Action.values()[actionNumber];
            this.continueAction = false;
        }
        else {
            this.continueAction = true;
        }

        switch (this.currentAction) {
            case VIEW_ROOMS:
                return this.getRooms();
            case CREATE_RES:
                return this.createReservation(argsList);
            case ALTER_RES:
                return this.alterReservation(argsList);
            case CANCEL_RES:
                return this.cancelReservation(argsList);
            case VIEW_RES:
                return this.viewReservation(argsList);
            case VIEW_REVENUE:
                return this.viewRevenue();
        }
        return "No matching action was found.";
    }

    private String getRooms() {
        String retString = "";

        //Default successful return
        this.actionResult = Results.SUCCESS;
        return retString;
    }

    // TODO: Refactor into more digestible functions
    private String createReservation(List<String> argsList) {
        int totalOcc;
        String retString = "";
        String availableRoomQuery;
        List<Room> validRooms = new ArrayList<Room>();
        Room desiredRoom = null;

        availableRoomQuery = "with TakenRooms as (SELECT DISTINCT Room FROM lab7_reservations WHERE " +
                "CheckIn >= ? AND CheckIn < ? OR " +
                "CheckOut > ? AND CheckOut <= ?) " +
                "SELECT RoomCode FROM lab7_rooms as r WHERE r.roomCode NOT IN (SELECT Room FROM TakenRooms) AND " +
                "r.maxOcc >= ?";

        if (this.continueAction) {
            //For confirmation/other reservation selection
            if (argsList.get(0).toLowerCase().equals("y")) {
                retString += "Your reservation has been confirmed.\n";
            }
            else {
                retString += "You have cancelled your reservation request.\n";
            }
        }
        else {
            //For initial reservation request
            try (Connection conn = DriverManager.getConnection(System.getenv("APP_JDBC_URL"),
                    System.getenv("APP_JDBC_USER"),
                    System.getenv("APP_JDBC_PW")))
            {
                totalOcc = Integer.parseInt(argsList.get(6)) +                        //argsList 6 and 7 are occupants
                    Integer.parseInt(argsList.get(7));

                if (argsList.get(2).length() > 0) {
                    desiredRoom = Room.fromDatabase(argsList.get(2), conn);
                }

                //Need to get valid rooms to check if requested room is available
                PreparedStatement availRooms = conn.prepareStatement(availableRoomQuery);


                availRooms.setDate(1, java.sql.Date.valueOf(argsList.get(4))); //argsList 4 is checkIn date
                availRooms.setDate(2, java.sql.Date.valueOf(argsList.get(5))); //argsList 5 is checkOut date
                availRooms.setDate(3, java.sql.Date.valueOf(argsList.get(4)));
                availRooms.setDate(4, java.sql.Date.valueOf(argsList.get(5)));
                availRooms.setInt(5, totalOcc);

                ResultSet rooms = availRooms.executeQuery();

                while(rooms.next()) {
                    validRooms.add(Room.fromDatabase(rooms.getString("RoomCode"), conn));
                }

                //FR2 requirement that requests with occupancies greater than max room size have custom fail message
                if (validRooms.size() == 0) {
                    retString += "We're sorry, but there are no rooms currently available that can fit\n" +
                            "your entire party. You'll need to make multiple reservations and split\n" +
                            "your party among multiple rooms.\n";
                    this.actionResult = Results.FAIL;
                    return retString;
                }
                else {
                    for (Room room : validRooms) {
                        if (desiredRoom != null && desiredRoom.getCode().equals(room.getCode())) {
                            retString += "Your desired room is available in your specified time frame.\n";
                            retString += "Here are the details of your reservation:\n";
                            retString += "Reserved by: " + argsList.get(0) + ", " + argsList.get(1) + "\n";
                            retString += "Booking room: " + desiredRoom.getName() + " (Code: " +
                                    desiredRoom.getCode() + ") \n";
                            retString += "Bed type: " + desiredRoom.getBedType() + "\n";
                            retString += "Start date: " + argsList.get(4) + "\n";
                            retString += "End date: " + argsList.get(5) + "\n";
                            retString += "Number of adults: " + argsList.get(7) + "\n";
                            retString += "Number of children: " + argsList.get(6) + "\n";
                            // TODO: Display cost information as well
                            this.actionResult = Results.PROMPT_AGAIN;
                            return retString;
                        }
                    }
                }
            }
            catch (SQLException e)
            {
                ExceptionReporter rp = new ExceptionReporter(e);

                rp.report();
                System.exit(-1);
            }
        }

        //Default successful return
        this.actionResult = Results.SUCCESS;
        return retString;
    }

    private String alterReservation(List<String> argsList) {
        String retString = "";

        if (this.continueAction) {
            //For confirmation of alteration
        }
        else {
            //For initial alteration request
        }

        //Default successful return
        this.actionResult = Results.SUCCESS;
        return retString;
    }

    private String cancelReservation(List<String> argsList) {
        String retString = "temp";

        if (this.continueAction) {
            //For cancellation confirmation
        }
        else {
            //For initial cancellation request
        }

        //Default successful return
        this.actionResult = Results.SUCCESS;
        return retString;
    }

    private String viewReservation(List<String> argsList) {
        String retString = "";

        //Default successful return
        this.actionResult = Results.SUCCESS;
        return retString;
    }

    private String viewRevenue() {
        String retString = "";

        //Default successful return
        this.actionResult = Results.SUCCESS;
        return retString;
    }

    public Action getCurrentAction()         { return currentAction; }
    public Results getActionResult()         { return actionResult; }
    public List<String> getPrevArgs()        { return prevArgs;}
}
